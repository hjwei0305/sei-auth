package com.changhong.sei.auth.certification.sso.workplus;

import com.changhong.sei.auth.certification.AbstractTokenAuthenticator;
import com.changhong.sei.auth.certification.sso.SingleSignOnAuthenticator;
import com.changhong.sei.auth.config.properties.AuthProperties;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.entity.ClientDetail;
import com.changhong.sei.auth.service.AccountService;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.HttpUtils;
import com.changhong.sei.core.util.JsonUtils;
import com.changhong.sei.core.utils.ResultDataUtil;
import com.changhong.sei.util.IdGenerator;
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
 * 实现功能：WorkPlus单点集成
 * 单点登录地址:  http://域名/api-gateway/sei-auth/sso/login?authType=workplus
 * <br>
 * <a href="https://open.workplus.io/light-app/sso.html#%E4%BB%80%E4%B9%88%E6%98%AF%E5%8D%95%E7%82%B9%E7%99%BB%E5%BD%95">文档地址</a>
 *
 * @author xiaogang.su
 * @version 1.0.00  2022-03-16 14:31
 */
@Lazy
@Component(SingleSignOnAuthenticator.AUTH_TYPE_WORKPLUS)
public class WorkPlusAuthenticator extends AbstractTokenAuthenticator implements SingleSignOnAuthenticator {
    private static final Logger LOG = LoggerFactory.getLogger(WorkPlusAuthenticator.class);

    /**
     * workplus工作台API访问地址
     */
    private String workPlusApiHost;

    private final AuthProperties authProperties;
    private final AccountService accountService;

    public WorkPlusAuthenticator(AuthProperties authProperties, AccountService accountService) {
        this.authProperties = authProperties;
        this.accountService = accountService;
        this.workPlusApiHost = ContextUtil.getProperty("sei.auth.sso.workplus.api.host");
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
        return null;
    }

    /**
     * 登录失败url地址
     *
     * @param userResponse 用户登录失败返回信息.可能为空,注意检查
     */
    @Override
    public String getLogoutUrl(SessionUserResponse userResponse, boolean agentIsMobile, HttpServletRequest request) {
        return null;
    }

    /**
     * 获取用户信息
     */
    @Override
    public ResultData<SessionUserResponse> auth(HttpServletRequest request) {
        // 应用代码
        String appCode = request.getParameter("appCode");
        if (StringUtils.isBlank(appCode)) {
            return ResultData.fail("appCode不能为空.");
        }
        ClientDetail clientDetail = clientDetailService.getByAppCode(appCode);
        if (Objects.isNull(clientDetail)) {
            return ResultData.fail("应用[" + appCode + "]未授权.");
        }
        if (StringUtils.isBlank(workPlusApiHost)) {
            return ResultData.fail("workplus工作台API访问地址不能为空.");
        }
        //1.请求workplus的API，通过app的key和secret获取accessToken
        ResultData<String> resultData = WorkPlusApiUtils.getAccessToken(workPlusApiHost, request.getParameter("domain_id"), request.getParameter("org_id"), clientDetail.getClientSecret(), clientDetail.getClientId());
        if (resultData.failed()) {
            return ResultDataUtil.fail(resultData.getMessage());
        }
        String token = resultData.getData();
        //2.请求workplus的API，验证APP传过来的ticket是否正确
        resultData = WorkPlusApiUtils.checkTicket(workPlusApiHost, request.getParameter("ticket"), token);
        if (resultData.failed()) {
            return ResultDataUtil.fail(resultData.getMessage());
        }
        String userId = resultData.getData();
        //3.获取用户信息
        resultData = WorkPlusApiUtils.getUserInfo(workPlusApiHost, userId, token);
        if (resultData.failed()) {
            return ResultDataUtil.fail(resultData.getMessage());
        }
        String account = resultData.getData();
        Account accountObj = accountService.getByAccountAndTenantCode(account, clientDetail.getTenantCode());
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
