package com.changhong.sei.auth.certification.sso.wechat;

import com.changhong.sei.auth.certification.AbstractTokenAuthenticator;
import com.changhong.sei.auth.certification.sso.SingleSignOnAuthenticator;
import com.changhong.sei.auth.common.weixin.WeChatUtil;
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
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 实现功能：企业微信单点集成
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-23 17:27
 */
@Component("weChatAuthenticator")
public class WeChatAuthenticator extends AbstractTokenAuthenticator implements SingleSignOnAuthenticator {
    private static final Logger LOG = LoggerFactory.getLogger(WeChatAuthenticator.class);
    private static final String CACHE_KEY = "WeChat:AccessToken";
    private static final String SOCIAL_CHANNEL = "WeChat";

    private final SocialAccountService socialAccountService;
    private final CacheBuilder cacheBuilder;

    public WeChatAuthenticator(SocialAccountService socialAccountService, CacheBuilder cacheBuilder) {
        this.socialAccountService = socialAccountService;
        this.cacheBuilder = cacheBuilder;
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
        return "/binding/socialAccount";
    }

    /**
     * 绑定账号
     */
    @Override
    public ResultData<String> bindingAccount(String tenant, String account, String password, String openId) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setTenant(tenant);
        loginRequest.setAccount(account);
        loginRequest.setPassword(password);
        loginRequest.setReqId(openId);
        ResultData<SessionUserResponse> resultData = login(loginRequest);
        if (resultData.successful()) {
            SessionUserResponse response = resultData.getData();
            ResultData<String> rd = socialAccountService.bindingAccount(response.getTenantCode(), response.getAccount(), openId, SOCIAL_CHANNEL);
            if (rd.successful()) {
                return ResultData.success(response.getSessionId());
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
    public ResultData<SessionUserResponse> auth(HttpServletRequest request, HttpServletResponse response) {
        // todo 配置
        String cropId = "wwdc99e9511ccac381";
        String agentId = "1000003";
        String cropSecret = "xIKMGprmIKWrK1VJ5oALdgeUAFng3DzxIpmPgT56XAA";

        // 授权码
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        // 检查缓存中是否存在有效token
        String accessToken = cacheBuilder.get(CACHE_KEY);
        if (StringUtils.isBlank(accessToken)) {
            // 不存在,获取新的有效token
            accessToken = WeChatUtil.getAccessToken(cropId, cropSecret);
            if (StringUtils.isNotBlank(accessToken)) {
                // 微信默认token过期时间为7200秒, 为防止过期缓存有效时间设置为7000秒
                cacheBuilder.set(CACHE_KEY, accessToken, 7000000);
            }
        }

        Map<String, Object> userMap = WeChatUtil.getUserInfo(accessToken, code);
        LOG.debug(JsonUtils.toJson(userMap));
        // 社交平台开放ID
        String openId = (String) userMap.get("OpenId");
        // 检查是否有账号绑定
        ResultData<Account> resultData = socialAccountService.checkAccount(SOCIAL_CHANNEL, openId);
        if (resultData.successful()) {
            Account account = resultData.getData();
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setTenant(account.getTenantCode());
            loginRequest.setAccount(account.getAccount());
            loginRequest.setReqId(code);
            return login(loginRequest, account);
        }

        return ResultData.fail(resultData.getMessage());
    }
}
