package com.changhong.sei.auth.certification.sso.sei;

import com.changhong.sei.auth.certification.AbstractTokenAuthenticator;
import com.changhong.sei.auth.certification.sso.Oauth2Authenticator;
import com.changhong.sei.auth.certification.sso.SingleSignOnAuthenticator;
import com.changhong.sei.auth.common.Constants;
import com.changhong.sei.auth.config.properties.AuthProperties;
import com.changhong.sei.auth.dto.ChannelEnum;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.HttpUtils;
import com.changhong.sei.core.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 实现功能: 默认OAuth2单点集成
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-23 17:27
 */
@Lazy
@Component(SingleSignOnAuthenticator.AUTH_TYPE_SEI_OAUTH2)
public class DefaultOAuth2Authenticator extends AbstractTokenAuthenticator implements Oauth2Authenticator, SingleSignOnAuthenticator, Constants {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultOAuth2Authenticator.class);

    public final String authorizeUrl;
    public final String accessUrl;
    public final String profileUrl;

    @Value("${sei.auth.sso.oauth2.client:none}")
    private String clientId;
    @Value("${sei.auth.sso.oauth2.secret:none}")
    private String clientSecret;

    private final AuthProperties properties;

    public DefaultOAuth2Authenticator(AuthProperties properties) {
        this.properties = properties;
        //
        this.authorizeUrl = ContextUtil.getProperty("sei.auth.sso.oauth2.authorize");
        // 通过授权码获取OAuth2认证服务器token地址
        this.accessUrl = ContextUtil.getProperty("sei.auth.sso.oauth2.access");
        // 通过token获取OAuth2认证服务器用户信息地址
        this.profileUrl = ContextUtil.getProperty("sei.auth.sso.oauth2.profile");
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
    public String getIndexUrl(SessionUserResponse userResponse, boolean agentIsMobile, HttpServletRequest request) {
        String url;
        if (agentIsMobile) {
            // 移动
            url = getAppBaseUrl() + "/#/main?sid=" + userResponse.getSessionId();
        } else {
            // PC
            url = getWebBaseUrl() + "/#/sso/ssoWrapperPage?sid=" + userResponse.getSessionId();
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("单点登录跳转地址: {}", url);
        }
        //单点错误页面
        return url;
    }

    /**
     * openId绑定失败,需要跳转到绑定页面
     *
     * @param userResponse 用户登录失败返回信息.可能为空,注意检查
     */
    @Override
    public String getLogoutUrl(SessionUserResponse userResponse, boolean agentIsMobile, HttpServletRequest request) {
        String url = properties.getSso().getLogout();
        if (LOG.isInfoEnabled()) {
            LOG.info("单点登录失败：跳转至登录页面: {}", url);
        }
        return url;
    }

    /**
     * 返回获取授权码页面地址
     * https://loginuatin.newhopedairy.cn/siam/oauth2.0/authorize?client_id=AUTH_CGFSSC&redirect_uri=http://10.200.16.10/api-gateway/sei-auth/sso/login?authType=oauth2&response_type=code
     */
    @Override
    public String getAuthorizeEndpoint(HttpServletRequest request) {
        Map<String, String> data = getAuthorizeData(request).getData();
        //这个方法的三个参数分别是授权后的重定向url、获取用户信息类型和state
        String url = "%s?client_id=%s&redirect_uri=%s&response_type=code&state=%s";

        String redirectUrl = String.format(url, authorizeUrl, clientId, data.get("redirect_uri"), data.get("state"));
        if (LOG.isInfoEnabled()) {
            LOG.info("获取授权码页面地址: {}", redirectUrl);
        }
        return redirectUrl;
    }

    @Override
    public ResultData<Map<String, String>> getAuthorizeData(HttpServletRequest request) {
        // 定义的一个参数，用户可以传入自定义的参数
        String state = "sei";
        // 用户OAuth2认证授权登录后重定向的页面路由
        String redirectUri = String.format("%s%s%s?authType=%s", getApiBaseUrl(), request.getContextPath(), SSO_LOGIN_ENDPOINT, SingleSignOnAuthenticator.AUTH_TYPE_SEI_OAUTH2);
        if (LOG.isInfoEnabled()) {
            LOG.info("获取授权码后重定向的页面路由: {}", redirectUri);
        }
        try {
            redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Map<String, String> data = new HashMap<>();
        // 重定向地址，需要进行UrlEncode
        data.put("redirect_uri", redirectUri);
        // 用于保持请求和回调的状态，授权请求后原样带回给企业。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议企业带上该参数，可设置为简单的随机数加session进行校验
        data.put("state", state);
        return ResultData.success(data);
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
        String code = request.getParameter("code");
        if (StringUtils.isNotBlank(code)) {
            // 单点登录地址
            String redirectUri = String.format("%s%s%s?authType=%s", getApiBaseUrl(), request.getContextPath(), SSO_LOGIN_ENDPOINT, SingleSignOnAuthenticator.AUTH_TYPE_SEI_OAUTH2);

            try {
                // 取得有效的AccessToken值
                StringBuilder url = new StringBuilder();
                url.append(accessUrl)
                        .append("?client_id=").append(clientId)
                        .append("&client_secret=").append(clientSecret)
                        .append("&grant_type=authorization_code&redirect_uri=").append(redirectUri)
                        .append("&code=").append(code);
                if (LOG.isInfoEnabled()) {
                    LOG.info("取得有效的AccessToken值URL:{}", url);
                }
                String postResult = HttpUtils.sendPost(url.toString(), "");
                if (LOG.isInfoEnabled()) {
                    LOG.info("取得有效的AccessToken值: {}", postResult);
                }
                Map<String, String> data = JsonUtils.fromJson(postResult, HashMap.class);
                if (data != null) {
                    // access_token：访问令牌
                    String accessToken = data.get("access_token");
                    if (StringUtils.isNotBlank(accessToken) && accessToken.contains("access_token=")) {
                        accessToken = accessToken.replaceAll("access_token=", "");
                    }
                    url.delete(0, url.length());
                    url.append(profileUrl).append("?access_token=").append(accessToken);
                    // 用户信息
                    postResult = HttpUtils.sendGet(url.toString(), "");
                    if (LOG.isInfoEnabled()) {
                        LOG.info("通过AccessToken获取用户信息: {}", postResult);
                    }
                    Map<String, String> userMap = JsonUtils.fromJson(postResult, HashMap.class);
                    if (LOG.isInfoEnabled()) {
                        LOG.info("UserInfo: {}", JsonUtils.toJson(userMap));
                    }
                    // OAuth2认证用户信息
                    String openId = userMap.get("id");

                    SessionUserResponse userResponse = new SessionUserResponse();
                    userResponse.setLoginStatus(SessionUserResponse.LoginStatus.failure);
                    userResponse.setOpenId(openId);
                    if (LOG.isInfoEnabled()) {
                        LOG.info("OpenId: {}", openId);
                    }

                    // 检查是否有账号绑定
                    ResultData<Account> resultData = accountService.checkAccount(ChannelEnum.SEI, openId);
                    if (LOG.isInfoEnabled()) {
                        LOG.info("检查是否有账号绑定: {}", resultData);
                    }
                    if (resultData.successful()) {
                        Account account = resultData.getData();

                        LoginRequest loginRequest = new LoginRequest();
                        loginRequest.setTenant(account.getTenantCode());
                        loginRequest.setAccount(account.getAccount());
                        loginRequest.setReqId(code);
                        ResultData<SessionUserResponse> result = login(loginRequest, account);
                        if (LOG.isInfoEnabled()) {
                            LOG.info("OAuth2认证中心账号登录验证结果: {}", result);
                        }
                        userResponse.setTenantCode(account.getTenantCode());
                        userResponse.setAccount(account.getAccount());
                        userResponse.setLoginAccount(account.getOpenId());
                        userResponse.setUserId(account.getUserId());
                        userResponse.setUserName(account.getName());
                        SessionUserResponse sessionUserResponse = result.getData();
                        if (Objects.nonNull(sessionUserResponse)) {
                            userResponse.setSessionId(sessionUserResponse.getSessionId());
                            userResponse.setUserType(sessionUserResponse.getUserType());
                            userResponse.setAuthorityPolicy(sessionUserResponse.getAuthorityPolicy());
                            userResponse.setLoginStatus(sessionUserResponse.getLoginStatus());
                        }
                    }
                    return ResultData.success(userResponse);
                } else {
                    return ResultData.fail("单点登录失败:" + JsonUtils.toJson(data));
                }
            } catch (Exception e) {
                return ResultData.fail("单点登录异常: " + ExceptionUtils.getRootCauseMessage(e));
            }
        } else {
            return ResultData.fail("单点登录失败:未获取到OAuth认证服务器的授权码");
        }
    }

    /**
     * 绑定账号
     */
    @Override
    public ResultData<SessionUserResponse> bindingAccount(LoginRequest loginRequest, boolean agentIsMobile) {
        return ResultData.fail("OAuth2认证不需要.");
    }

    @Override
    public ResultData<Map<String, String>> jsapi_ticket() {
        return ResultData.fail("OAuth2认证不需要.");
    }

}
