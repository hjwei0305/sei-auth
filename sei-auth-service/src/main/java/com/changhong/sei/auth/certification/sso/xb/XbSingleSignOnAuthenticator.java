package com.changhong.sei.auth.certification.sso.xb;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.changhong.sei.auth.certification.AbstractTokenAuthenticator;
import com.changhong.sei.auth.certification.sso.SingleSignOnAuthenticator;
import com.changhong.sei.auth.config.properties.AuthProperties;
import com.changhong.sei.auth.config.properties.SsoProperties;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.service.AccountService;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.HttpUtils;
import com.changhong.sei.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author Joe
 * @date 2022/9/14
 */
@Lazy
@Component(SingleSignOnAuthenticator.AUTH_TYPE_XB_GT)
public class XbSingleSignOnAuthenticator extends AbstractTokenAuthenticator implements SingleSignOnAuthenticator {

    private static final Logger LOG = LoggerFactory.getLogger(XbSingleSignOnAuthenticator.class);
    private final AuthProperties authProperties;
    private final SsoProperties properties;
    private final AccountService accountService;

    public XbSingleSignOnAuthenticator(AuthProperties authProperties, SsoProperties properties, AccountService accountService) {
        this.authProperties = authProperties;
        this.properties = properties;
        this.accountService = accountService;
    }

    @Override
    public String getWebBaseUrl() {
        return authProperties.getWebBaseUrl();
    }

    @Override
    public String getAppBaseUrl() {
        return authProperties.getAppBaseUrl();
    }

    @Override
    public String getLogoutUrl(SessionUserResponse userResponse, boolean agentIsMobile, HttpServletRequest request) {
        return properties.getLogout();
    }

    @Override
    public ResultData<SessionUserResponse> auth(HttpServletRequest request) {
        //获取单点参数
        String ssoToken = request.getParameter("token");
        String tenant = request.getParameter("tenant");
        LOG.debug("SSO token {}", ssoToken);
        String url = "http://192.168.117.139:9999/api/XBLoginCheck/Check?ticket=";
        String userCode = "";
        //发送http请求
        String result = HttpUtils.get(url + ssoToken,null,null,10000,10000);
        String str1 = result.replace("\"","");
        //解析工号
        userCode = ""+str1 ;
        if (!StringUtils.isBlank(userCode)) {
            Account accountObj = accountService.getByAccountAndTenantCode(userCode, tenant);
            if (Objects.nonNull(accountObj)) {

                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setTenant(accountObj.getTenantCode());
                loginRequest.setReqId(IdGenerator.uuid2());
                return login(loginRequest, accountObj);
            }
            else {
                LOG.error("账号[{}]不存在，单点登录失败.", userCode);
                return ResultData.fail("账号[" + userCode + "]不存在，单点登录失败.");
            }
        } else {
            return ResultData.fail("未拿到单点登录的工号！！！");
        }
    }

    @Override
    public ResultData<SessionUserResponse> auth(LoginRequest loginParam) {
        return null;
    }
}
