package com.changhong.sei.auth.certification.sso.wechat;

import com.changhong.sei.auth.certification.AbstractTokenAuthenticator;
import com.changhong.sei.auth.certification.sso.SingleSignOnAuthenticator;
import com.changhong.sei.auth.common.Constants;
import com.changhong.sei.auth.common.weixin.WeChatUtil;
import com.changhong.sei.auth.config.properties.AuthProperties;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.service.SocialAccountService;
import com.changhong.sei.core.cache.CacheBuilder;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;

/**
 * 实现功能：企业微信单点集成
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-23 17:27
 */
@Component(SingleSignOnAuthenticator.AUTH_TYPE_WE_CHAT)
public class WeChatAuthenticator extends AbstractTokenAuthenticator implements SingleSignOnAuthenticator, Constants {
    private static final Logger LOG = LoggerFactory.getLogger(WeChatAuthenticator.class);
    private static final String CACHE_KEY_TOKEN = "WeChat:AccessToken";
    private static final String SOCIAL_CHANNEL = "WeChat";

//    private String cropId = "wwdc99e9511ccac381";
//    private String agentId = "1000003";
//    private String cropSecret = "xIKMGprmIKWrK1VJ5oALdgeUAFng3DzxIpmPgT56XAA";

    private final SocialAccountService socialAccountService;
    private final CacheBuilder cacheBuilder;
    private final AuthProperties properties;

    public WeChatAuthenticator(AuthProperties properties, SocialAccountService socialAccountService, CacheBuilder cacheBuilder) {
        this.socialAccountService = socialAccountService;
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
     * 登录成功url地址
     */
    @Override
    public String getIndexUrl() {
        return null;
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
       /*
        http://tsei.changhong.com:8090/api-gateway/sei-auth/sso/login
        https://open.weixin.qq.com/connect/oauth2/authorize?appid=wwdc99e9511ccac381&redirect_uri=http%3A%2F%2Ftsei.changhong.com%3A8090%2Fapi-gateway%2Fsei-auth%2Fsso%2Flogin&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect
         */
        //这个方法的三个参数分别是授权后的重定向url、获取用户信息类型和state
        String redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect";
        // 微信定义的一个参数，用户可以传入自定义的参数
        String state = "sei";
        // 用户微信授权登录后重定向的页面路由
        String redirect_uri = request.getRequestURL().toString().replace(AUTHORIZE_ENDPOINT, SSO_LOGIN_ENDPOINT);
        try {
            redirectUrl = String.format(redirectUrl, properties.getSso().getAppId(), URLEncoder.encode(redirect_uri, "UTF-8"), state);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return redirectUrl;
    }

    /**
     * 绑定账号
     */
    @Override
    public ResultData<SessionUserResponse> bindingAccount(LoginRequest loginRequest) {
        // 社交平台开放ID
        String openId = loginRequest.getReqId();
        ResultData<SessionUserResponse> resultData = login(loginRequest);
        if (resultData.successful()) {
            SessionUserResponse response = resultData.getData();
            ResultData<String> rd = socialAccountService.bindingAccount(response.getTenantCode(), response.getAccount(), openId, SOCIAL_CHANNEL);
            if (rd.successful()) {
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
        String accessToken = cacheBuilder.get(CACHE_KEY_TOKEN);
        if (StringUtils.isBlank(accessToken)) {
            // 不存在,获取新的有效token
            accessToken = WeChatUtil.getAccessToken(properties.getSso().getAppId(), properties.getSso().getCropSecret());
            if (StringUtils.isNotBlank(accessToken)) {
                // 微信默认token过期时间为7200秒, 为防止过期缓存有效时间设置为7000秒
                cacheBuilder.set(CACHE_KEY_TOKEN, accessToken, 7000000);
            }
        }

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
        ResultData<Account> resultData = socialAccountService.checkAccount(SOCIAL_CHANNEL, openId);
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
            SessionUserResponse sessionUserResponse = result.getData();
            if (Objects.nonNull(sessionUserResponse)) {
                userResponse.setSessionId(sessionUserResponse.getSessionId());
                userResponse.setLoginStatus(sessionUserResponse.getLoginStatus());
            }
        }
        return ResultData.success(userResponse);
    }
}
