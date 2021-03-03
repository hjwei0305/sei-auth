package com.changhong.sei.auth.certification.sso.sei;

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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 实现功能：预制单点集成
 * 配置中心增加配置:
 * sei.sso.security = 秘钥
 * sei.auth.sso.tenant = 租户代码
 * 单点登录地址:  http://域名/api-gateway/sei-auth/sso/login?authType=sei
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-14 14:31
 */
@Component(SingleSignOnAuthenticator.AUTH_TYPE_SEI)
public class DefaultAuthenticator extends AbstractTokenAuthenticator implements SingleSignOnAuthenticator {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAuthenticator.class);

    private static final String PARAM_TOKEN = "token";
    private static String SECURITY;
    private final AuthProperties authProperties;
    private final SsoProperties properties;
    private final AccountService accountService;

    public DefaultAuthenticator(AuthProperties authProperties, SsoProperties properties, AccountService accountService) {
        this.authProperties = authProperties;
        this.properties = properties;
        this.accountService = accountService;

        //配置中心读取秘钥
        SECURITY = properties.getSecurity();
        if (StringUtils.isBlank(SECURITY)) {
            LOG.error("未配置单点认证秘钥[sei.sso.security]！");
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
        String token = request.getParameter(PARAM_TOKEN);
        if (LOG.isDebugEnabled()) {
            LOG.debug("token ：{}", token);
        }

        String account;
        if (StringUtils.isNotBlank(token)) {
            String decryptStr;
            try {
                decryptStr = DesUtil.decrypt(token, SECURITY);
            } catch (Exception e) {
                LOG.error("单点认证失败，token验证异常!", e);
                return ResultData.fail("单点认证失败，token验证异常!");
            }
            LOG.info("单点token解析：{}", decryptStr);
            // 人员编号+系统标识+时间戳 如：zs|erp|1521448142833
            if (StringUtils.isNotBlank(decryptStr) && decryptStr.indexOf("|") > 0) {
                String[] arr = decryptStr.split("[|]");
                if (arr.length > 1) {
                    account = arr[0];

                    LOG.info("SSO登录用户：{}", account);
                } else {
                    LOG.error("单点认证失败，非法的签名token[" + token + "]");
                    return ResultData.fail("单点认证失败，非法的签名!");
                }
            } else {
                LOG.error("单点认证失败，非法的签名token[" + token + "]");
                return ResultData.fail("单点认证失败，非法的签名!");
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
