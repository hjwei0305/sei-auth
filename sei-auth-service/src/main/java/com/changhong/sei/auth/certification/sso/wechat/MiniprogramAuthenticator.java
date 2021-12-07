package com.changhong.sei.auth.certification.sso.wechat;

import com.changhong.sei.auth.certification.AbstractTokenAuthenticator;
import com.changhong.sei.auth.certification.sso.Oauth2Authenticator;
import com.changhong.sei.auth.certification.sso.SingleSignOnAuthenticator;
import com.changhong.sei.auth.common.Constants;
import com.changhong.sei.auth.config.properties.AuthProperties;
import com.changhong.sei.auth.dto.BindingAccountRequest;
import com.changhong.sei.auth.dto.ChannelEnum;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.entity.ClientDetail;
import com.changhong.sei.core.cache.CacheBuilder;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.HttpUtils;
import com.changhong.sei.core.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 实现功能：微信小程序单点集成
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-23 17:27
 */
@Lazy
@Component(SingleSignOnAuthenticator.AUTH_TYPE_WX_MINI_PROGRAM)
public class MiniprogramAuthenticator extends AbstractTokenAuthenticator implements Oauth2Authenticator, SingleSignOnAuthenticator, Constants {
    private static final Logger LOG = LoggerFactory.getLogger(MiniprogramAuthenticator.class);
    private static final String CACHE_KEY_TOKEN = "miniprogram:session_key:";

//    private String cropId = "wwdc99e9511ccac381";
//    private String agentId = "1000003";
//    private String cropSecret = "xIKMGprmIKWrK1VJ5oALdgeUAFng3DzxIpmPgT56XAA";

    /**
     * 获取访问用户
     */
    private static final String GET_USER_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    private final CacheBuilder cacheBuilder;
    private final AuthProperties properties;

    public MiniprogramAuthenticator(AuthProperties properties, CacheBuilder cacheBuilder) {
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
    public String getIndexUrl(SessionUserResponse userResponse, boolean agentIsMobile, HttpServletRequest request) {
        return null;
    }

    /**
     * openId绑定失败,需要跳转到绑定页面
     *
     * @param userResponse 用户登录失败返回信息.可能为空,注意检查
     */
    @Override
    public String getLogoutUrl(SessionUserResponse userResponse, boolean agentIsMobile, HttpServletRequest request) {
        return null;
    }

    /**
     * openId绑定失败,需要跳转到绑定页面
     */
    @Override
    public String getAuthorizeEndpoint(HttpServletRequest request) {
        return null;
    }

    @Override
    public ResultData<Map<String, String>> getAuthorizeData(HttpServletRequest request) {
        return ResultData.fail("not support.");
    }

    /**
     * 绑定账号
     */
    @Override
    public ResultData<SessionUserResponse> bindingAccount(LoginRequest loginRequest, boolean agentIsMobile) {
        // 社交平台开放ID
        String openId = loginRequest.getReqId();
        // 小程序绑定需要传小程序应用代码
        String appCode = loginRequest.getAppCode();
        ResultData<SessionUserResponse> resultData = login(loginRequest);
        if (resultData.successful()) {
            SessionUserResponse response = resultData.getData();
            if (Objects.nonNull(response) && SessionUserResponse.LoginStatus.success.equals(response.getLoginStatus())) {
                BindingAccountRequest accountRequest = new BindingAccountRequest();
                accountRequest.setTenantCode(response.getTenantCode());
                accountRequest.setAccount(response.getAccount());
                accountRequest.setUserId(response.getUserId());
                accountRequest.setName(response.getUserName());
                accountRequest.setOpenId(this.getAppOpenId(appCode, openId));
                accountRequest.setChannel(ChannelEnum.WXMiniProgram);

                ResultData<String> rd = accountService.bindingAccount(accountRequest);
                if (rd.successful()) {
                    //设置跳转地址
                    String url;
                    if (agentIsMobile) {
                        // 移动
                        url = getAppBaseUrl() + "/#/main?sid=" + response.getSessionId();
                    } else {
                        // PC
                        url = getWebBaseUrl() + "/#/sso/ssoWrapperPage?sid=" + response.getSessionId();
                    }
                    LOG.info("单点登录跳转地址: {}", url);

                    response.setRedirectUrl(url);
                    return ResultData.success(response);
                } else {
                    return ResultData.fail(rd.getMessage());
                }
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
    @SuppressWarnings("unchecked")
    @Override
    public ResultData<SessionUserResponse> auth(HttpServletRequest request) {
        // 授权码
        String code = request.getParameter("code");
        // 应用代码(小程序定制必传)
        String appCode = request.getParameter("appCode");
        if (StringUtils.isBlank(appCode)) {
            return ResultData.fail("appCode不能为空.");
        }
        ClientDetail clientDetail = clientDetailService.getByAppCode(appCode);
        if (Objects.isNull(clientDetail)) {
            return ResultData.fail("应用[" + appCode + "]未授权.");
        }
        String url = String.format(GET_USER_URL, clientDetail.getClientId(), clientDetail.getClientSecret(), code);
        Map<String, Object> userMap;
        LOG.info("小程序认证请求: {}", url);
        try {
            String result = HttpUtils.sendGet(url);
            LOG.info("小程序认证请求结果: {}", result);
            userMap = JsonUtils.fromJson(result, HashMap.class);
        } catch (Exception e) {
            LOG.error("发起微信平台请求[" + url + "]异常.", e);
            return ResultData.fail("发起微信平台请求[" + url + "]异常.");
        }
        if (Objects.isNull(userMap)) {
            return ResultData.fail("小程序认证失败.");
        }
        LOG.info("UserInfo: {}", JsonUtils.toJson(userMap));
        String openId = (String) userMap.get("openid");
        String sessionKey = (String) userMap.get("session_key");
        // 暂存sessionKey
        this.setSessionKey(openId, sessionKey);

        SessionUserResponse userResponse = new SessionUserResponse();
        // 未绑定
        userResponse.setLoginStatus(SessionUserResponse.LoginStatus.noBind);
        userResponse.setOpenId(openId);
        LOG.info("OpenId: {}", openId);

        // 检查是否有账号绑定
        ResultData<Account> resultData = accountService.checkAccount(ChannelEnum.WXMiniProgram, this.getAppOpenId(appCode, openId));
        LOG.info("检查是否有账号绑定: {}", resultData);

        if (resultData.successful()) {
            Account account = resultData.getData();

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setTenant(account.getTenantCode());
            loginRequest.setAccount(openId);
            loginRequest.setReqId(code);
            ResultData<SessionUserResponse> result = login(loginRequest, account);
            LOG.info("微信关联账号登录验证: {}", result);
            userResponse.setTenantCode(account.getTenantCode());
            userResponse.setAccount(account.getAccount());
            userResponse.setLoginAccount(openId);
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
    }

    @Override
    public ResultData<Map<String, String>> jsapi_ticket() {
        return null;
    }

    /**
     * 暂存SessionKey
     */
    private void setSessionKey(String openId, String sessionKey) {
        // 微信默认SessionKey过期时间为3天
        cacheBuilder.set(CACHE_KEY_TOKEN, sessionKey, 259200);
    }

    /**
     * 获取SessionKey
     */
    private String getSessionKey(String openId) {
        // 检查缓存中是否存在有效SessionKey
        return cacheBuilder.get(CACHE_KEY_TOKEN);
    }

    /**
     * 因一家企业可能存在多个小程序.
     * 因此,当channel为小程序时,openId为小程序代码+"|"+小程序返回的openId
     *
     * @param appCode 小程序应用代码
     * @param openId  小程序返回openId
     * @return 返回sei约定的小程序openId
     */
    private String getAppOpenId(String appCode, String openId) {
        return appCode + "|" + openId;
    }
}
