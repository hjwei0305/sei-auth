package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.api.AuthenticationApi;
import com.changhong.sei.auth.certification.TokenAuthenticatorBuilder;
import com.changhong.sei.auth.certification.sso.sei.DesUtil;
import com.changhong.sei.auth.common.Constants;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.OAuth2Response;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.entity.AuthClient;
import com.changhong.sei.auth.entity.vo.AuthorizeCodeVo;
import com.changhong.sei.auth.service.AccountService;
import com.changhong.sei.auth.service.AuthClientService;
import com.changhong.sei.auth.service.SessionService;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.log.annotation.AccessLog;
import com.changhong.sei.core.util.HttpUtils;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDateTime;
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
    private AuthClientService authClientService;
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
        AuthClient authClient = authClientService.getByClientId(tenantCode, clientId);
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
     *
     * @param clientId     应用标示
     * @param url          授权后重定向的回调链接地址，请使用urlencode对链接进行处理
     * @param responseType 返回类型[授权码:code 隐藏式:token 密码式:password 客户端凭证:client_credentials],固定为code
     * @param scope        应用授权作用域
     * @param state        重定向后会带上state参数，可以填写a-zA-Z0-9的参数值，长度不可超过128个字节
     */
    @Override
    public ResultData<String> authorize(String tenantCode, String clientId, String url, String responseType, String scope,
                                        String state, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 授权码模式
        if (StringUtils.equalsIgnoreCase("code", responseType)) {
            AuthClient authClient = authClientService.getByClientId(tenantCode, clientId);
            if (Objects.isNull(authClient)) {
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
            String allowUrl = authClient.getAllowUrl();
            if (StringUtils.isNotBlank(allowUrl)) {
                String[] allowArr = allowUrl.split("[,]");
                if (!StringUtils.containsAny(url, allowArr)) {
                    // 非法redirect_url
                    return ResultData.fail(ContextUtil.getMessage("oauth2_0004", url));
                }
            }
            // 生成授权码
            String code = IdGenerator.uuid2();
            AuthorizeCodeVo vo = new AuthorizeCodeVo();
            vo.setTenantCode(tenantCode);
            vo.setCode(code);
            vo.setClientId(clientId);
            vo.setScope(scope);
            vo.setRedirectUri(url);
            // 存入redis缓存
            redisTemplate.boundValueOps(Constants.OAUTH2_CODE_KEY.concat(code)).set(vo, 3, TimeUnit.MINUTES);

            // 重定向授权，下放code
            String redirectUri = joinParam(url, code);
            if (StringUtils.isNotBlank(state)) {
                redirectUri = joinParam(redirectUri, "state", state);
            }
            // 发送重定向
            response.sendRedirect(redirectUri);
            return null;
        } else {
            // 不支持的OAuth2认证方式
            return ResultData.fail(ContextUtil.getMessage("oauth2_0003"));
        }
    }

    /**
     * 通过授权码请求token
     *
     * @param clientId     应用标示
     * @param clientSecret 应用秘钥
     * @param grantType    授权方式
     * @param code         授权码
     * @param redirectUri  授权后重定向的回调链接地址，请使用urlencode对链接进行处理
     */
    @Override
    public ResultData<OAuth2Response> token(String tenantCode, String clientId, String clientSecret, String grantType,
                                            String code, String redirectUri, HttpServletRequest request) {
        if (StringUtils.equalsIgnoreCase("authorization_code", grantType)) {
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
            AuthClient authClient = authClientService.getByClientId(tenantCode, clientId);
            if (Objects.isNull(authClient) || !StringUtils.equals(clientSecret, authClient.getClientSecret())) {
                // 无效client_secret
                return ResultData.fail(ContextUtil.getMessage("oauth2_0006", clientId));
            }

            SessionUser sessionUser = new SessionUser();
            sessionUser.setTenantCode(tenantCode);
            sessionUser.setUserId(clientId);
            // 当前登录账号
            sessionUser.setLoginAccount(clientId);
            sessionUser.setAccount(clientId);
            sessionUser.setUserName(clientId);
            sessionUser.setIp(HttpUtils.getClientIP(request));
            ContextUtil.generateToken(sessionUser);

            // 生成会话token
            sessionService.addSession(sessionUser, TimeUnit.HOURS.toMillis(2));
            OAuth2Response oAuth2Response = new OAuth2Response(LocalDateTime.now().plusHours(2));
            oAuth2Response.setSid(sessionUser.getSessionId());
            oAuth2Response.setAccessToken(sessionUser.getToken());
            return ResultData.success(oAuth2Response);
        } else {
            // 不支持的OAuth2认证方式
            return ResultData.fail(ContextUtil.getMessage("oauth2_0003"));
        }
    }

    // /**
    //  * 凭证式
    //  *
    //  * @param clientId 应用标示
    //  * @param secret   应用秘钥
    //  * @param scope    应用授权作用域
    //  */
    // @Override
    // public ResultData<OAuth2Response> clientToken(String tenantCode, String clientId, String secret, String scope) {
    //     RSAUtils.getKeys();
    //     return null;
    // }
    //
    // /**
    //  * 密码式
    //  *
    //  * @param clientId 应用标示
    //  * @param username 账号
    //  * @param secret 密码
    //  */
    // @Override
    // public ResultData<OAuth2Response> userToken(String tenantCode, String clientId, String username, String secret) {
    //     return null;
    // }
    //
    // /**
    //  * 回收token
    //  *
    //  * @param sid
    //  */
    // @Override
    // public ResultData<Void> revoke(String sid) {
    //     return null;
    // }
    //
    // /**
    //  * 确认授权接口
    //  */
    // @Override
    // public ResultData<Void> confirm() {
    //     return null;
    // }

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
}
