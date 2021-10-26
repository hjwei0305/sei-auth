package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.api.AuthenticationApi;
import com.changhong.sei.auth.certification.TokenAuthenticatorBuilder;
import com.changhong.sei.auth.certification.sso.sei.DesUtil;
import com.changhong.sei.auth.common.Constants;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.OAuth2Response;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.entity.ClientDetail;
import com.changhong.sei.auth.entity.vo.AuthorizeCodeVo;
import com.changhong.sei.auth.service.AccountService;
import com.changhong.sei.auth.service.ApprovalsService;
import com.changhong.sei.auth.service.ClientDetailService;
import com.changhong.sei.auth.service.SessionService;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.log.annotation.AccessLog;
import com.changhong.sei.core.util.HttpUtils;
import com.changhong.sei.core.util.JsonUtils;
import com.changhong.sei.exception.WebException;
import com.changhong.sei.util.IdGenerator;
import com.changhong.sei.util.thread.ThreadLocalUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 14:21
 */
@RestController
@Api(value = "AuthenticationApi", tags = "账户认证服务")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController implements AuthenticationApi {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private TokenAuthenticatorBuilder authenticatorBuilder;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientDetailService authClientService;
    @Autowired
    private ApprovalsService approvalsService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 登录
     * 1.账户认证
     * 2.产生会话
     * 3.会话写入缓存并设置有效期
     * 4.返回会话id
     */
    @Override
    @AccessLog(AccessLog.FilterReply.DENY)
    public ResultData<SessionUserResponse> login(LoginRequest loginRequest) {
        LogUtil.bizLog("登录请求: {}", JsonUtils.toJson(loginRequest));
        HttpServletRequest request = HttpUtils.getRequest();
        // 客户端ip
        ThreadLocalUtil.setTranVar("ClientIP", HttpUtils.getClientIP(request));
        // 浏览器信息
        ThreadLocalUtil.setTranVar("UserAgent", HttpUtils.getUserAgent(request));

        ResultData<SessionUserResponse> resultData = authenticatorBuilder.getAuthenticator(loginRequest.getAuthType()).auth(loginRequest);
        SessionUserResponse userResponse = resultData.getData();
        if (Objects.nonNull(userResponse)) {
            // 设置当前环境
            userResponse.setEnv(ContextUtil.getProperty("spring.cloud.config.profile"));
        }
        LogUtil.bizLog("登录结果: {}", JsonUtils.toJson(resultData));
        return resultData;
    }

    /**
     * 登出
     * 清除会话id
     */
    @Override
    public ResultData<String> logout(String sid) {
        LogUtil.bizLog("登出: {}", sid);
        try {
            sessionService.removeSession(sid, 0);
            return ResultData.success("OK");
        } catch (Exception e) {
            // 登出异常: {0}
            return ResultData.fail(ContextUtil.getMessage("authentication_0001", e.getMessage()));
        }
    }

    /**
     * 认证会话id
     * 1.通过id获取会话内容(未获取到直接返回false)
     * 2.刷新id的有效期
     * 3.返回true
     */
    @Override
    @AccessLog(AccessLog.FilterReply.DENY)
    public ResultData<String> check(String sid) {
        try {
            // 获取会话并续期
            String token = sessionService.touchSession(sid);
            if (StringUtils.isNotBlank(token)) {
                return ResultData.success(token);
            } else {
                // 认证失败
                return ResultData.fail(ContextUtil.getMessage("authentication_0002"));
            }
        } catch (Exception e) {
            // 认证会话id异常
            return ResultData.fail(ContextUtil.getMessage("authentication_0003", e.getMessage()));
        }
    }

    /**
     * 获取匿名token
     */
    @Override
    @AccessLog(AccessLog.FilterReply.DENY)
    public ResultData<String> getAnonymousToken() {
        SessionUser sessionUser = new SessionUser();
        sessionUser.setTenantCode("anonymous");
        sessionUser.setUserId("anonymous");
        sessionUser.setAccount("anonymous");
        sessionUser.setUserName("anonymous");
//        sessionUser.setEmail("anonymous");
        ContextUtil.generateToken(sessionUser);
        return ResultData.success(sessionUser.getToken());
    }

    /**
     * 获取指定会话用户信息
     */
    @Override
    public ResultData<SessionUserResponse> getSessionUser(String sid) {
        // 获取会话并续期
        String token = sessionService.touchSession(sid);
        if (StringUtils.isNotBlank(token)) {
            SessionUserResponse response = new SessionUserResponse();
            try {
                SessionUser user = ContextUtil.getSessionUser(token);
                modelMapper.map(user, response);

                return ResultData.success(response);
            } catch (Exception e) {
                LogUtil.error("获取会话信息异常", e);
                // 获取会话信息异常
                return ResultData.fail(ContextUtil.getMessage("authentication_0004", e.getMessage()));
            }
        } else {
            // 无会话信息
            return ResultData.fail(ContextUtil.getMessage("authentication_0005"));
        }
    }

    /**
     * 通过账号签名方式认证
     *
     * @param tenantCode 租户代码
     * @param clientId   应用标示
     * @param account    账号
     * @param stamp      时间戳
     * @param sign       签名.
     */
    @Override
    public ResultData<OAuth2Response> signToken(String tenantCode, String clientId, String account, Long stamp, String sign,
                                                HttpServletRequest request) {
        ClientDetail authClient = authClientService.getByClientId(tenantCode, clientId);
        if (Objects.isNull(authClient)) {
            // 无效client_id
            return ResultData.fail(ContextUtil.getMessage("oauth2_0002", clientId));
        }
        String localSign = null;
        StringBuilder str = new StringBuilder(32);
        str.append(tenantCode).append(clientId).append(account).append(stamp);
        try {
            localSign = DesUtil.encrypt(str.toString(), authClient.getClientSecret());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 对比签名值
        if (StringUtils.equals(localSign, sign)) {
            Account entity = accountService.getByAccountAndTenantCode(account, tenantCode);
            if (Objects.isNull(entity)) {
                // 认证失败
                return ResultData.fail(ContextUtil.getMessage("authentication_0002"));
            }

            SessionUser sessionUser = new SessionUser();
            sessionUser.setTenantCode(tenantCode);
            sessionUser.setUserId(entity.getUserId());
            // 当前登录账号
            sessionUser.setLoginAccount(entity.getOpenId());
            sessionUser.setAccount(entity.getAccount());
            sessionUser.setUserName(entity.getName());
            sessionUser.setIp(HttpUtils.getClientIP(request));
            ContextUtil.generateToken(sessionUser);

            // 生成会话token
            sessionService.addSession(sessionUser, TimeUnit.HOURS.toMillis(2));
            OAuth2Response oAuth2Response = new OAuth2Response(LocalDateTime.now().plusHours(2));
            oAuth2Response.setSid(sessionUser.getSessionId());
            oAuth2Response.setAccessToken(sessionUser.getToken());
            return ResultData.success(oAuth2Response);
        } else {
            // 认证失败
            return ResultData.fail(ContextUtil.getMessage("authentication_0002"));
        }
    }

    /**
     * OAuth 2.0 的四种方式
     * 授权码:指第三方应用先申请一个授权码，然后再用该码获取token.安全性高,可以避免token泄漏
     * 隐藏式:直接向前端颁发token.并以Hash的形式存放在重定向uri的fargment中发送给浏览器
     * 密码式:高度信任某个应用.直接通过账号密码给出token
     * 凭证式:适用于没有前端的命令行应用(即在后端服务中请求令牌,针对第三方应用的，而不是针对用户的)。验证通过以后，直接返回令牌
     */
    @Override
    public Object oauth2(String tenantCode, String apiPath, HttpServletRequest request, HttpServletResponse response) {
        // 按类型路由
        String responseType = request.getParameter(Constants.OAuth2Param.response_type);
        String grantType = request.getParameter(Constants.OAuth2Param.grant_type);
        // 模式一：Code授权码
        if (StringUtils.equals(Constants.OAuth2Api.authorize, apiPath) && StringUtils.equals(Constants.OAuth2ResponseType.code, responseType)) {
            return this.authorize(tenantCode, request, response);
        }
        // Code授权码 获取 Access-Token
        else if (StringUtils.equals(Constants.OAuth2Api.token, apiPath) && StringUtils.equals(Constants.OAuth2GrantType.authorization_code, grantType)) {
            return token(tenantCode, request, response);
        }
        // Refresh-Token 刷新 Access-Token
        else if (StringUtils.equals(Constants.OAuth2Api.refresh, apiPath) && StringUtils.equals(Constants.OAuth2GrantType.refresh_token, grantType)) {
            return refreshToken(tenantCode, request);
        }
        // doConfirm 确认授权接口
        else if (StringUtils.equals(Constants.OAuth2Api.doConfirm, apiPath)) {
            return doConfirm(tenantCode, request);
        }
        // 模式二：隐藏式
        else if (StringUtils.equals(Constants.OAuth2Api.authorize, apiPath) && StringUtils.equals(Constants.OAuth2ResponseType.token, responseType)) {
            return authorize(tenantCode, request, response);
        }
        // 模式三：密码式
        else if (StringUtils.equals(Constants.OAuth2Api.token, apiPath) && StringUtils.equals(Constants.OAuth2GrantType.password, grantType)) {
            return password(tenantCode, request, response);
        }
        // 模式四：凭证式
        else if (StringUtils.equals(Constants.OAuth2Api.client_token, apiPath) && StringUtils.equals(Constants.OAuth2GrantType.client_credentials, grantType)) {
            return clientToken(tenantCode, request);
        }
        // 默认返回
        return ResultData.fail("错误的oauth2请求.");
    }

    /**
     * 模式一：Code授权码 / 模式二：隐藏式
     *
     * @param request  请求对象
     * @param response 响应对象
     * @return 处理结果
     */
    private Object authorize(String tenantCode, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> data = new HashMap<>();
        data.put("tenantCode", tenantCode);
        data.put("baseUrl", request.getRequestURL().toString().replace(request.getServletPath(), "/"));

        // 如果尚未登录, 则先去登录
        String sid = HttpUtils.readCookieValue(Constants.COOKIE_SID, request);
        if (StringUtils.isBlank(sid)) {
            // 去登录页面
            return new ModelAndView("login.html", data);
        }
        byte[] decodeCookieBytes = Base64.getDecoder().decode(sid);
        sid = new String(decodeCookieBytes);
        SessionUser sessionUser = sessionService.getSessionUser(sid);
        if (sessionUser.isAnonymous()) {
            // 去登录页面
            return new ModelAndView("login.html", data);
        }

        String clientId = request.getParameter(Constants.OAuth2Param.client_id);
        String url = request.getParameter(Constants.OAuth2Param.redirect_uri);
        String scope = request.getParameter(Constants.OAuth2Param.scope);
        String state = request.getParameter(Constants.OAuth2Param.state);

        ClientDetail clientDetail = authClientService.getByClientId(tenantCode, clientId);
        if (Objects.isNull(clientDetail)) {
            // 无效client_id
            return ResultData.fail(ContextUtil.getMessage("oauth2_0002", clientId));
        }
        // 1、是否是一个有效的url
        if (!isUrl(url)) {
            // 无效redirect_url
            return ResultData.fail(ContextUtil.getMessage("oauth2_0001", url));
        }
        // 2、截取掉?后面的部分
        int index = url.indexOf("?");
        if (index != -1) {
            url = url.substring(0, index);
        }
        // 3、是否在[允许地址列表]之中
        String allowUrl = clientDetail.getAllowUrl();
        if (StringUtils.isNotBlank(allowUrl)) {
            String[] allowArr = allowUrl.split("[,]");
            if (!StringUtils.containsAny(url, allowArr)) {
                // 非法redirect_url
                return ResultData.fail(ContextUtil.getMessage("oauth2_0004", url));
            }
        }

        // 4、校验：此次申请的Scope，该Client是否已经签约

        // 5、判断：如果此次申请的Scope，该用户尚未授权，则转到授权页面
        boolean isGrant = approvalsService.isGrant(tenantCode, clientId, sessionUser.getUserId(), scope);
        if (!isGrant) {
            data.put("userId", sessionUser.getUserId());
            data.put("clientId", clientId);
            data.put("clientName", clientDetail.getClientName());
            data.put("scope", scope);
            // 去授权页面
            return new ModelAndView("approvals.html", data);
        }

        // 生成授权码
        String code = IdGenerator.uuid2();
        AuthorizeCodeVo vo = new AuthorizeCodeVo();
        vo.setTenantCode(tenantCode);
        vo.setCode(code);
        vo.setClientId(clientId);
        vo.setScope(scope);
        vo.setRedirectUri(url);
        vo.setUserId(sessionUser.getUserId());
        vo.setAccount(sessionUser.getAccount());
        vo.setLoginAccount(sessionUser.getLoginAccount());
        vo.setUserName(sessionUser.getUserName());
        vo.setUserType(sessionUser.getUserType());
        vo.setAuthorityPolicy(sessionUser.getAuthorityPolicy());
        vo.setIp(HttpUtils.getClientIP(request));
        vo.setLocale(sessionUser.getLocale());
        // 存入redis缓存
        redisTemplate.boundValueOps(Constants.OAUTH2_CODE_KEY.concat(code)).set(vo, 5, TimeUnit.MINUTES);

        String redirectUri = request.getParameter(Constants.OAuth2Param.redirect_uri);
        String responseType = request.getParameter(Constants.OAuth2Param.response_type);
        // 如果是 授权码式，则：开始重定向授权，下放code
        if (Constants.OAuth2ResponseType.code.equals(responseType)) {
            // 重定向授权，下放code
            redirectUri = joinParam(redirectUri, Constants.OAuth2Param.code, code);
            if (StringUtils.isNotBlank(state)) {
                redirectUri = joinParam(redirectUri, Constants.OAuth2Param.scope, state);
            }
        }
        // 如果是 隐藏式，则：开始重定向授权，下放 token
        else if (Constants.OAuth2ResponseType.token.equals(responseType)) {
            redirectUri = buildImplicitRedirectUri(redirectUri, sid, state);
        } else {
            // 默认返回
            throw new WebException("无效response_type: " + responseType);
        }
        // 发送重定向
        try {
            response.sendRedirect(redirectUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Code授权码 获取 Access-Token
     *
     * @param req 请求对象
     * @param res 响应对象
     * @return 处理结果
     */
    private Object token(String tenantCode, HttpServletRequest req, HttpServletResponse res) {
        // 获取参数
        String code = req.getParameter(Constants.OAuth2Param.code);
        String clientId = req.getParameter(Constants.OAuth2Param.client_id);
        String clientSecret = req.getParameter(Constants.OAuth2Param.client_secret);
        String redirectUri = req.getParameter(Constants.OAuth2Param.redirect_uri);

        String redisKey = Constants.OAUTH2_CODE_KEY.concat(code);
        AuthorizeCodeVo authorizeCodeVo = (AuthorizeCodeVo) redisTemplate.boundValueOps(redisKey).get();
        // 删除授权码缓存.一个授权码只能使用一次
        redisTemplate.delete(redisKey);
        if (Objects.isNull(authorizeCodeVo)) {
            // 无效的OAuth2授权码
            return ResultData.fail(ContextUtil.getMessage("oauth2_0005", code));
        }
        if (!StringUtils.equals(tenantCode, authorizeCodeVo.getTenantCode())) {
            // 无效的OAuth2授权码
            return ResultData.fail(ContextUtil.getMessage("oauth2_0005", code));
        }
        // 如果提供了redirectUri，则校验其是否与请求Code时提供的一致
        if (StringUtils.isNotBlank(redirectUri)) {
            if (!StringUtils.equals(redirectUri, authorizeCodeVo.getRedirectUri())) {
                // 非法redirect_url
                return ResultData.fail(ContextUtil.getMessage("oauth2_0004", redirectUri));
            }
        }
        if (!StringUtils.equals(clientId, authorizeCodeVo.getClientId())) {
            // 无效的client_id
            return ResultData.fail(ContextUtil.getMessage("oauth2_0002", clientId));
        }

        // 校验：Secret是否正确
        ClientDetail clientDetail = authClientService.getByClientId(tenantCode, clientId);
        if (Objects.isNull(clientDetail) || !StringUtils.equals(clientSecret, clientDetail.getClientSecret())) {
            // 无效client_secret
            return ResultData.fail(ContextUtil.getMessage("oauth2_0006", clientId));
        }

        SessionUser sessionUser = new SessionUser();
        sessionUser.setTenantCode(tenantCode);
        sessionUser.setUserId(authorizeCodeVo.getUserId());
        sessionUser.setAccount(authorizeCodeVo.getAccount());
        sessionUser.setLoginAccount(authorizeCodeVo.getLoginAccount());
        sessionUser.setUserName(authorizeCodeVo.getUserName());
        sessionUser.setUserType(authorizeCodeVo.getUserType());
        sessionUser.setAuthorityPolicy(authorizeCodeVo.getAuthorityPolicy());
        sessionUser.setIp(authorizeCodeVo.getIp());
        sessionUser.setLocale(authorizeCodeVo.getLocale());
        // 生成会话token
        sessionService.addSession(sessionUser, TimeUnit.HOURS.toMillis(2));
        OAuth2Response oAuth2Response = new OAuth2Response(LocalDateTime.now().plusHours(2));
        oAuth2Response.setSid(sessionUser.getSessionId());
        oAuth2Response.setAccessToken(sessionUser.getToken());
        oAuth2Response.setUserId(sessionUser.getUserId());
        oAuth2Response.setAccount(sessionUser.getAccount());
        return ResultData.success(oAuth2Response);
    }

    /**
     * Refresh-Token 刷新 Access-Token
     *
     * @param req 请求对象
     * @return 处理结果
     */
    private Object refreshToken(String tenantCode, HttpServletRequest req) {
        // 获取参数
        String clientId = req.getParameter(Constants.OAuth2Param.client_id);
        String clientSecret = req.getParameter(Constants.OAuth2Param.client_secret);
        String refreshToken = req.getParameter(Constants.OAuth2Param.refresh_token);

        // 校验参数

        // todo 获取新Token返回
        return null;
    }

    /**
     * doConfirm 确认授权接口
     *
     * @param req 请求对象
     * @return 处理结果
     */
    private Object doConfirm(String tenantCode, HttpServletRequest req) {
        String clientId = req.getParameter(Constants.OAuth2Param.client_id);
        String scope = req.getParameter(Constants.OAuth2Param.scope);
        String userId = req.getParameter(Constants.OAuth2Param.userId);
        return approvalsService.saveGrantScope(tenantCode, clientId, userId, scope);
    }

    /**
     * 模式三：密码式
     *
     * @param req 请求对象
     * @param res 响应对象
     * @return 处理结果
     */
    private Object password(String tenantCode, HttpServletRequest req, HttpServletResponse res) {
        // 1、获取请求参数
        String clientId = req.getParameter(Constants.OAuth2Param.client_id);
        String username = req.getParameter(Constants.OAuth2Param.username);
        String password = req.getParameter(Constants.OAuth2Param.password);

        // 2、校验client_id
        ClientDetail clientDetail = authClientService.getByClientId(tenantCode, clientId);
        if (Objects.isNull(clientDetail)) {
            // 无效client_id
            return ResultData.fail(ContextUtil.getMessage("oauth2_0002", clientId));
        }

        // 3、调用API 开始登录，如果没能成功登录，则直接退出
        HttpServletRequest request = HttpUtils.getRequest();
        // 客户端ip
        ThreadLocalUtil.setTranVar("ClientIP", HttpUtils.getClientIP(request));
        // 浏览器信息
        ThreadLocalUtil.setTranVar("UserAgent", HttpUtils.getUserAgent(request));
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setReqId(IdGenerator.uuid2());
        loginRequest.setTenant(tenantCode);
        loginRequest.setAccount(username);
        loginRequest.setPassword(password);
        ResultData<SessionUserResponse> resultData = authenticatorBuilder.getAuthenticator(Constants.OAuth2Param.password)
                .auth(loginRequest);
        if (resultData.successful()) {
            SessionUserResponse userResponse = resultData.getData();
            // 生成会话token
            OAuth2Response oAuth2Response = new OAuth2Response(LocalDateTime.now().plusHours(2));
            oAuth2Response.setSid(userResponse.getSessionId());
            oAuth2Response.setAccessToken(sessionService.touchSession(userResponse.getSessionId()));
            oAuth2Response.setUserId(userResponse.getUserId());
            oAuth2Response.setAccount(userResponse.getAccount());
            // 返回 Access-Token
            return ResultData.success(oAuth2Response);
        } else {
            return resultData;
        }
    }

    /**
     * 模式四：凭证式
     *
     * @param req 请求对象
     * @return 处理结果
     */
    private Object clientToken(String tenantCode, HttpServletRequest req) {
        // 获取参数
        String clientId = req.getParameter(Constants.OAuth2Param.client_id);
        String clientSecret = req.getParameter(Constants.OAuth2Param.client_secret);
        String scope = req.getParameter(Constants.OAuth2Param.scope);

        // 校验 ClientSecret
        ClientDetail clientDetail = authClientService.getByClientId(tenantCode, clientId);
        if (Objects.isNull(clientDetail) || !StringUtils.equals(clientSecret, clientDetail.getClientSecret())) {
            // 无效client_secret
            return ResultData.fail(ContextUtil.getMessage("oauth2_0006", clientId));
        }

        // 返回 Client-Token
        SessionUser sessionUser = new SessionUser();
        sessionUser.setTenantCode(tenantCode);
        sessionUser.setUserId(clientId);
        // 当前登录账号
        sessionUser.setLoginAccount(clientId);
        sessionUser.setAccount(clientId);
        sessionUser.setUserName(clientId);
        sessionUser.setIp(HttpUtils.getClientIP(req));
        ContextUtil.generateToken(sessionUser);

        // 生成会话token
        sessionService.addSession(sessionUser, TimeUnit.HOURS.toMillis(2));
        OAuth2Response oAuth2Response = new OAuth2Response(LocalDateTime.now().plusHours(2));
        oAuth2Response.setSid(sessionUser.getSessionId());
        oAuth2Response.setAccessToken(sessionUser.getToken());
        // 返回 Client-Token
        return ResultData.success(oAuth2Response);
    }

    /**
     * 验证URL的正则表达式
     */
    private static final String URL_REGEX = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";

    /**
     * 使用正则表达式判断一个字符串是否为URL
     *
     * @param str 字符串
     * @return 拼接后的url字符串
     */
    private boolean isUrl(String str) {
        if (str == null) {
            return false;
        }
        return str.toLowerCase().matches(URL_REGEX);
    }

    /**
     * URL编码
     *
     * @param url see note
     * @return see note
     */
    private String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new WebException("URL编码异常", e);
        }
    }

    /**
     * URL解码
     *
     * @param url see note
     * @return see note
     */
    private String decoderUrl(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new WebException("URL解码异常", e);
        }
    }

    /**
     * 在url上拼接上kv参数并返回
     *
     * @param url      url
     * @param paramStr 参数, 如 id=1001
     * @return 拼接后的url字符串
     */
    private static String joinParam(String url, String paramStr) {
        // 如果参数为空, 直接返回
        if (StringUtils.isBlank(paramStr)) {
            return url;
        }
        if (url == null) {
            url = "";
        }
        int index = url.lastIndexOf('?');
        // ? 不存在
        if (index < 0) {
            return url + '?' + paramStr;
        }
        // ? 是最后一位
        else if (index == url.length() - 1) {
            return url + paramStr;
        }
        // ? 是其中一位
        else {
            String separatorChar = "&";
            // 如果最后一位是 不是&, 且 paramStr 第一位不是 &, 就增送一个 &
            if (url.lastIndexOf(separatorChar) != url.length() - 1 && paramStr.indexOf(separatorChar) != 0) {
                return url + separatorChar + paramStr;
            } else {
                return url + paramStr;
            }
        }
    }

    /**
     * 在url上拼接上kv参数并返回
     *
     * @param url   url
     * @param key   参数名称
     * @param value 参数值
     * @return 拼接后的url字符串
     */
    private static String joinParam(String url, String key, Object value) {
        // 如果参数为空, 直接返回
        if (StringUtils.isBlank(url) || StringUtils.isBlank(key) || Objects.isNull(value)) {
            return url;
        }
        return joinParam(url, key + "=" + value);
    }

    /**
     * 在url上拼接锚参数
     *
     * @param url       url
     * @param parameStr 参数, 例如 id=1001
     * @return 拼接后的url字符串
     */
    private static String joinSharpParam(String url, String parameStr) {
        // 如果参数为空, 直接返回
        if (parameStr == null || parameStr.length() == 0) {
            return url;
        }
        if (url == null) {
            url = "";
        }
        int index = url.lastIndexOf('#');
        // ? 不存在
        if (index == -1) {
            return url + '#' + parameStr;
        }
        // ? 是最后一位
        if (index == url.length() - 1) {
            return url + parameStr;
        }
        // ? 是其中一位
        if (index > -1 && index < url.length() - 1) {
            String separatorChar = "&";
            // 如果最后一位是 不是&, 且 parameStr 第一位不是 &, 就增送一个 &
            if (url.lastIndexOf(separatorChar) != url.length() - 1 && parameStr.indexOf(separatorChar) != 0) {
                return url + separatorChar + parameStr;
            } else {
                return url + parameStr;
            }
        }
        // 正常情况下, 代码不可能执行到此
        return url;
    }

    /**
     * 在url上拼接锚参数
     *
     * @param url   url
     * @param key   参数名称
     * @param value 参数值
     * @return 拼接后的url字符串
     */
    private static String joinSharpParam(String url, String key, Object value) {
        // 如果参数为空, 直接返回
        if (StringUtils.isBlank(url) || StringUtils.isBlank(key) || Objects.isNull(value)) {
            return url;
        }
        return joinSharpParam(url, key + "=" + value);
    }

    /**
     * 构建URL：下放Access-Token URL （implicit 隐藏式）
     *
     * @param redirectUri 下放地址
     * @param token       token
     * @param state       state参数
     * @return 构建完毕的URL
     */
    private static String buildImplicitRedirectUri(String redirectUri, String token, String state) {
        String url = joinSharpParam(redirectUri, Constants.OAuth2Param.token, token);
        if (StringUtils.isNotBlank(state)) {
            url = joinSharpParam(url, Constants.OAuth2Param.state, state);
        }
        return url;
    }
}
