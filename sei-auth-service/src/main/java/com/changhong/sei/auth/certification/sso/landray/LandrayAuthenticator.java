package com.changhong.sei.auth.certification.sso.landray;

import com.changhong.sei.auth.certification.AbstractTokenAuthenticator;
import com.changhong.sei.auth.certification.sso.SingleSignOnAuthenticator;
import com.changhong.sei.auth.config.properties.AuthProperties;
import com.changhong.sei.auth.config.properties.SsoProperties;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.service.AccountService;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.util.IdGenerator;
import com.landray.sso.client.token.LtpaTokenGenerator;
import com.landray.sso.client.token.Token;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 实现功能：蓝凌OA单点集成
 * 配置中心增加配置:
 * sei.sso.security = 秘钥
 * sei.auth.sso.tenant = 租户代码
 * 单点登录地址:  http://域名/api-gateway/sei-auth/sso/login?authType=landray
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-14 14:31
 */
@Lazy
@Component(SingleSignOnAuthenticator.AUTH_TYPE_LANDRAY)
public class LandrayAuthenticator extends AbstractTokenAuthenticator implements SingleSignOnAuthenticator {
    private static final Logger LOG = LoggerFactory.getLogger(LandrayAuthenticator.class);

    public static final String COOKIE_NAME = "LtpaToken";
    private final AuthProperties authProperties;
    private final SsoProperties properties;
    private final AccountService accountService;
    private final LtpaTokenGenerator ltpaTokenGenerator;

    public LandrayAuthenticator(AuthProperties authProperties, SsoProperties properties, AccountService accountService) {
        this.authProperties = authProperties;
        this.properties = properties;
        this.accountService = accountService;
        this.ltpaTokenGenerator = new LtpaTokenGenerator();

        //配置中心读取秘钥
        String security = properties.getSecurity();
        if (StringUtils.isBlank(security)) {
            LOG.error("未配置单点认证秘钥[sei.sso.security]！");
        } else {
            this.ltpaTokenGenerator.setSecurityKey(security);
            this.ltpaTokenGenerator.setCookieName(COOKIE_NAME);
        }
    }

    /**
     * 前端web根url地址.必须
     * 如:http://tsei.changhong.com:8090/sei-portal-web
     */
    @Override
    public String getWebBaseUrl() {
        return authProperties.getWebBaseUrl();
    }

    /**
     * APP根url地址.必须
     * 如:http://tsei.changhong.com:8090/sei-app
     */
    @Override
    public String getAppBaseUrl() {
        return authProperties.getAppBaseUrl();
    }

    /**
     * 登录成功url地址
     */
    @Override
    public String getIndexUrl(SessionUserResponse userResponse, boolean agentIsMobile, HttpServletRequest request) {
        String url = properties.getIndex();
        // PC登录：跳转到新版(react)的页面
        if (StringUtils.isBlank(url)) {
            url = getWebBaseUrl() + "/#/sso/ssoWrapperPage?sid=" + userResponse.getSessionId();
        }
        LOG.info("单点登录跳转地址: {}", url);
        return url;
    }

    /**
     * 登录失败url地址
     *
     * @param userResponse 用户登录失败返回信息.可能为空,注意检查
     */
    @Override
    public String getLogoutUrl(SessionUserResponse userResponse, boolean agentIsMobile, HttpServletRequest request) {
        return properties.getLogout();
    }

    /**
     * 获取用户信息
     */
    @Override
    public ResultData<SessionUserResponse> auth(HttpServletRequest request) {
        //会话token
        String ltpaToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (StringUtils.equalsIgnoreCase(COOKIE_NAME, cookie.getName())) {
                ltpaToken = cookie.getValue();
                break;
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("LtpaToken ：{}", ltpaToken);
        }

        String account;
        if (StringUtils.isNotBlank(ltpaToken)) {
            try {
                Token token = ltpaTokenGenerator.generateTokenByTokenString(ltpaToken);
                LOG.debug("获得的token：{}", token);
                account = token.getUsername();
                LOG.info("SSO登录用户：{}", account);
            } catch (Exception e) {
                LOG.error("单点认证失败，token验证异常!", e);
                return ResultData.fail("单点认证失败，token验证异常!");
            }
        } else {
            LOG.error("单点认证失败，token不能为空!");
            return ResultData.fail("单点认证失败，参数不正确!");
        }

        Account accountObj = accountService.getByAccountAndTenantCode(account, properties.getTenant());
        if (Objects.nonNull(accountObj)) {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setTenant(accountObj.getTenantCode());
            loginRequest.setAccount(accountObj.getAccount());
            loginRequest.setReqId(IdGenerator.uuid2());
            return login(loginRequest, accountObj);
        } else {
            LOG.error("账号[{}]不存在，单点登录失败.", account);
            return ResultData.fail("账号[" + account + "]不存在，单点登录失败.");
        }
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
}
