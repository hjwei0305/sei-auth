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
    private static String SECURITY;

    public XbSingleSignOnAuthenticator(AuthProperties authProperties, SsoProperties properties, AccountService accountService) {
        this.authProperties = authProperties;
        this.properties = properties;
        this.accountService = accountService;

        //配置中心读取秘钥
        SECURITY = properties.getSecurity();
        if (org.apache.commons.lang3.StringUtils.isBlank(SECURITY)) {
            LOG.error("SEI默认单点集成未配置单点认证秘钥[sei.sso.security]！若未使用可忽略");
        }
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
    public String getIndexUrl(SessionUserResponse userResponse, boolean agentIsMobile, HttpServletRequest request) {
        String reUrl = userResponse.getRedirectUrl();
        String url;
        if(StringUtils.isNotBlank(reUrl)){
            url = getWebBaseUrl() + "/#/sso/subPageTurnPage?sid="+ userResponse.getSessionId()+"&redirectUrl="+reUrl;
        }else {
            url = getWebBaseUrl() + "/#/sso/ssoWrapperPage?sid=" + userResponse.getSessionId();
        }
        return url;
    }

    @Override
    public ResultData<SessionUserResponse> auth(HttpServletRequest request) {
        //网址中心传过来的加密工号
        String ssoToken = request.getParameter("eipaulg");
        //租户代码
        String tenant = request.getParameter("tenant");
        //需要跳转页面
        String modular = request.getParameter("modular");
        String reUrl = request.getParameter("reUrl");
        //解密工号网址
        String url = "http://192.168.117.139:9999/api/XBLoginCheck/Check?ticket=";
        String userCode = "";
        //发送http请求
        String result = HttpUtils.get(url + ssoToken,null,null,10000,10000);
        String str1 = result.replace("\"","");
        //解析工号
        userCode = str1 ;
        //判断工号是否存在
        if (!StringUtils.isBlank(userCode)) {
            Account accountObj = accountService.getByAccountAndTenantCode(userCode, tenant);
            if (Objects.nonNull(accountObj)) {

                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setTenant(accountObj.getTenantCode());
                loginRequest.setReqId(IdGenerator.uuid2());
                loginRequest.setAccount(userCode);
                ResultData<SessionUserResponse> user = login(loginRequest, accountObj);
                SessionUserResponse userResponse = new SessionUserResponse();
                userResponse.setTenantCode(accountObj.getTenantCode());
                userResponse.setAccount(accountObj.getAccount());
                userResponse.setLoginAccount(accountObj.getOpenId());
                userResponse.setUserId(accountObj.getUserId());
                userResponse.setUserName(accountObj.getName());
                SessionUserResponse sessionUserResponse = user.getData();
                if (Objects.nonNull(sessionUserResponse)) {
                    userResponse.setSessionId(sessionUserResponse.getSessionId());
                    userResponse.setUserType(sessionUserResponse.getUserType());
                    userResponse.setAuthorityPolicy(sessionUserResponse.getAuthorityPolicy());
                    userResponse.setLoginStatus(sessionUserResponse.getLoginStatus());
                }
                if(StringUtils.isNotBlank(reUrl)) {
                    reUrl = "/"+modular+"/#/"+reUrl;
                    user.getData().setRedirectUrl(reUrl);
                }
                return ResultData.success(userResponse);
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
