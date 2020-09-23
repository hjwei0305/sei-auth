package com.changhong.sei.auth.certification.sso.wechat;

import com.changhong.sei.auth.certification.AbstractTokenAuthenticator;
import com.changhong.sei.auth.certification.sso.Oauth2Authenticator;
import com.changhong.sei.auth.certification.sso.SingleSignOnAuthenticator;
import com.changhong.sei.auth.common.Constants;
import com.changhong.sei.auth.common.RandomUtils;
import com.changhong.sei.auth.common.weixin.WeChatUtil;
import com.changhong.sei.auth.config.properties.AuthProperties;
import com.changhong.sei.auth.dto.BindingAccountRequest;
import com.changhong.sei.auth.dto.ChannelEnum;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.core.cache.CacheBuilder;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.JsonUtils;
import com.changhong.sei.util.Signature;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 实现功能：企业微信单点集成
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-23 17:27
 */
@Component(SingleSignOnAuthenticator.AUTH_TYPE_WE_CHAT)
public class WeChatAuthenticator extends AbstractTokenAuthenticator implements Oauth2Authenticator, SingleSignOnAuthenticator, Constants {
    private static final Logger LOG = LoggerFactory.getLogger(WeChatAuthenticator.class);
    private static final String CACHE_KEY_TOKEN = "WeChat:AccessToken";

//    private String cropId = "wwdc99e9511ccac381";
//    private String agentId = "1000003";
//    private String cropSecret = "xIKMGprmIKWrK1VJ5oALdgeUAFng3DzxIpmPgT56XAA";

    private final CacheBuilder cacheBuilder;
    private final AuthProperties properties;

    public WeChatAuthenticator(AuthProperties properties, CacheBuilder cacheBuilder) {
        this.cacheBuilder = cacheBuilder;
        this.properties = properties;
    }


    /**
     * 前端web根url地址
     * 如:http://tsei.changhong.com:8090/sei-portal-web
     */
    @Override
    public String getWebBaseUrl() {
        return properties.getWebBaseUrl();
    }

    /**
     * APP根url地址
     * 如:http://tsei.changhong.com:8090/sei-app
     */
    @Override
    public String getAppBaseUrl() {
        return properties.getAppBaseUrl();
    }

    /**
     * 服务网关根url地址
     * 如:http://tsei.changhong.com:8090/api-gateway
     */
    @Override
    public String getApiBaseUrl() {
        return properties.getApiBaseUrl();
    }

    /**
     * 登录成功url地址
     */
    @Override
    public String getIndexUrl(SessionUserResponse userResponse) {
        String url = null;
        if (SessionUserResponse.LoginStatus.success == userResponse.getLoginStatus()) {
            url = getWebBaseUrl() + "/#/sso/ssoWrapperPage?sid=" + userResponse.getSessionId();
            LOG.error("单点登录跳转地址: {}", url);
        } else {
            if (StringUtils.isNotBlank(userResponse.getOpenId())) {
                // 账号绑定页面
                url = getWebBaseUrl() + "/#/sso/socialAccount?authType=" + SingleSignOnAuthenticator.AUTH_TYPE_WE_CHAT
                        + "&tenant=" + (StringUtils.isNotBlank(userResponse.getTenantCode()) ? userResponse.getTenantCode() : "")
                        + "&openId=" + (StringUtils.isNotBlank(userResponse.getOpenId()) ? userResponse.getOpenId() : "");
                LOG.error("单点登录失败：需要绑定平台账号！");
            }
        }
        //单点错误页面
        return url;
    }

    /**
     * openId绑定失败,需要跳转到绑定页面
     */
    @Override
    public String getLogoutUrl() {
        return null;
    }

    /**
     * openId绑定失败,需要跳转到绑定页面
     */
    @Override
    public String getAuthorizeEndpoint(HttpServletRequest request) {
        Map<String, String> data = getAuthorizeData(request).getData();
        //这个方法的三个参数分别是授权后的重定向url、获取用户信息类型和state
        String redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect";

        redirectUrl = String.format(redirectUrl, properties.getSso().getAppId(), data.get("redirect_uri"), data.get("state"));
        return redirectUrl;
    }

    @Override
    public ResultData<Map<String, String>> getAuthorizeData(HttpServletRequest request) {
       /*
        http://tsei.changhong.com:8090/api-gateway/sei-auth/sso/login?authType=weChat
        https://open.weixin.qq.com/connect/oauth2/authorize?appid=wwdc99e9511ccac381&redirect_uri=http%3A%2F%2Ftsei.changhong.com%3A8090%2Fapi-gateway%2Fsei-auth%2Fsso%2Flogin%3FauthType%3DweChat&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect
         */
        AuthProperties.SingleSignOnProperties sso = properties.getSso();

        // 微信定义的一个参数，用户可以传入自定义的参数
        String state = "sei";
        // 用户微信授权登录后重定向的页面路由
        String redirectUri = getApiBaseUrl() + request.getContextPath() + SSO_LOGIN_ENDPOINT + "?authType=weChat";
        try {
            redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Map<String, String> data = new HashMap<>();
        // 企业微信的CorpID，在企业微信管理端查看
        data.put("appid", sso.getAppId());
        // 授权方的网页应用ID，在具体的网页应用中查看
        data.put("agentid", sso.getAgentId());
        // 重定向地址，需要进行UrlEncode
        data.put("redirect_uri", redirectUri);
        // 用于保持请求和回调的状态，授权请求后原样带回给企业。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议企业带上该参数，可设置为简单的随机数加session进行校验
        data.put("state", state);
        // 自定义样式链接，企业可根据实际需求覆盖默认样式
        data.put("href", "");

        return ResultData.success(data);
    }

    /**
     * 绑定账号
     */
    @Override
    public ResultData<SessionUserResponse> bindingAccount(LoginRequest loginRequest, HttpServletRequest request) {
        // 社交平台开放ID
        String openId = loginRequest.getReqId();
        ResultData<SessionUserResponse> resultData = login(loginRequest);
        if (resultData.successful()) {
            SessionUserResponse response = resultData.getData();

            BindingAccountRequest accountRequest = new BindingAccountRequest();
            accountRequest.setTenantCode(response.getTenantCode());
            accountRequest.setAccount(response.getAccount());
            accountRequest.setUserId(response.getUserId());
            accountRequest.setName(response.getUserName());
            accountRequest.setAccountType(response.getUserType().name());
            accountRequest.setOpenId(openId);
            accountRequest.setChannel(ChannelEnum.WeChat);

            ResultData<String> rd = accountService.bindingAccount(accountRequest);
            if (rd.successful()) {
                //浏览器客户端信息
                String ua = request.getHeader("User-Agent");
                //设置跳转地址
                String url;
                if (checkAgentIsMobile(request.getHeader(ua)) ) {
                    // 移动
                    url = getAppBaseUrl() + "/#/main?sid=" + response.getSessionId();
                } else {
                    // PC
                    url = getWebBaseUrl() + "/#/sso/ssoWrapperPage?sid=" + response.getSessionId();
                }
                LOG.error("单点登录跳转地址: {}", url);

                response.setRedirectUrl(url);
                return ResultData.success(response);
            } else {
                return ResultData.fail(rd.getMessage());
            }
        }
        return ResultData.fail(resultData.getMessage());
    }

    /**
     * 获取用户信息
     *
     * @param loginParam 授权参数
     * @return LoginDTO
     */
    @Override
    public ResultData<SessionUserResponse> auth(LoginRequest loginParam) {
        return ResultData.fail("认证类型错误.");
    }

    /**
     * 获取用户信息
     */
    @Override
    public ResultData<SessionUserResponse> auth(HttpServletRequest request) {
        // 授权码
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        // 检查缓存中是否存在有效token
        String accessToken = getAccessToken();

        Map<String, Object> userMap = WeChatUtil.getUserInfo(accessToken, code);
        LOG.info("UserInfo: {}", JsonUtils.toJson(userMap));
        // 社交平台开放ID
        String openId = (String) userMap.get("UserId");
//        String openId = code;

        SessionUserResponse userResponse = new SessionUserResponse();
        userResponse.setLoginStatus(SessionUserResponse.LoginStatus.failure);
        userResponse.setOpenId(openId);
        LOG.info("OpenId: {}", openId);

        // 检查是否有账号绑定
        ResultData<Account> resultData = accountService.checkAccount(ChannelEnum.WeChat, openId);
        LOG.info("检查是否有账号绑定: {}", resultData);
        if (resultData.successful()) {
            Account account = resultData.getData();

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setTenant(account.getTenantCode());
            loginRequest.setAccount(account.getAccount());
            loginRequest.setReqId(code);
            ResultData<SessionUserResponse> result = login(loginRequest, account);
            LOG.info("微信关联账号登录验证: {}", result);
            userResponse.setTenantCode(account.getTenantCode());
            userResponse.setAccount(account.getAccount());
            userResponse.setLoginAccount(account.getOpenId());
            SessionUserResponse sessionUserResponse = result.getData();
            if (Objects.nonNull(sessionUserResponse)) {
                userResponse.setSessionId(sessionUserResponse.getSessionId());
                userResponse.setLoginStatus(sessionUserResponse.getLoginStatus());
            }
        }
        return ResultData.success(userResponse);
    }

    @Override
    public ResultData<Map<String, String>> jsapi_ticket() {
        ResultData<Map<String, String>> result;


        // 必填，生成签名的时间戳
        String timestamp = String.valueOf(System.currentTimeMillis());
        // 必填，生成签名的随机串
        String nonceStr = RandomUtils.randomString(16);
        String url = getWebBaseUrl() + "/";

        // 必填，签名，见 附录-JS-SDK使用权限签名算法
        String signature = "";
//        String agentSignature = "";

        String accessToken = getAccessToken();
        String ticket = WeChatUtil.getJsApiTicket(accessToken);
//        String agentTicket = WeChatUtil.getJsApiAppTicket(accessToken);
        if (StringUtils.isNotBlank(ticket)) {
            String str = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s", ticket, nonceStr, timestamp, url);
            signature = Signature.sign(str);
//            str = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s", agentTicket, nonceStr, timestamp, url);
//            agentSignature = Signature.sign(str);

            AuthProperties.SingleSignOnProperties sso = properties.getSso();

            Map<String, String> data = new HashMap<>();
            // 必填，企业微信的corpid，必须与当前登录的企业一致
            data.put("corpid", sso.getAppId());
            // 必填，企业微信的应用id （e.g. 1000247）
//            data.put("agentid", sso.getAgentId());
            // 必填，生成签名的时间戳
            data.put("timestamp", timestamp);
            // 必填，生成签名的随机串
            data.put("nonceStr", nonceStr);
            data.put("signature", signature);
//            data.put("agentSignature", agentSignature);
            data.put("ticket", ticket);
//            data.put("agentTicket", agentTicket);
            data.put("url", url);
            result = ResultData.success(data);
        } else {
            result = ResultData.fail("获取企业的jsapi_ticket异常");
        }
        return result;
    }

    /**
     * 获取AccessToken
     */
    private String getAccessToken() {
        // 检查缓存中是否存在有效token
        String accessToken = cacheBuilder.get(CACHE_KEY_TOKEN);
        if (StringUtils.isBlank(accessToken)) {
            AuthProperties.SingleSignOnProperties sso = properties.getSso();

            // 不存在,获取新的有效token
            accessToken = WeChatUtil.getAccessToken(sso.getAppId(), sso.getCropSecret());
            if (StringUtils.isNotBlank(accessToken)) {
                // 微信默认token过期时间为7200秒, 为防止过期缓存有效时间设置为7000秒
                cacheBuilder.set(CACHE_KEY_TOKEN, accessToken, 7000000);
            }
        }
        return accessToken;
    }
}
