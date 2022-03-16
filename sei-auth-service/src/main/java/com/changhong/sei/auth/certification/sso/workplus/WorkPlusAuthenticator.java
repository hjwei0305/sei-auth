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
import com.changhong.sei.util.IdGenerator;
import com.landray.sso.client.token.Token;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
     * 获取Token
     */
    private static final String GET_TOKEN_URL = "/v1/token";
    /**
     * ticket验证
     */
    private static final String TICKET_CHECK_URL = "/v1/tickets/%s?access_token=%s";
    /**
     * 获取用户信息
     */
    private static final String GET_USER_INFO_URL = "/v1/users/%s?access_token=%s&type=id";
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
        // 授权码
        String ticket = request.getParameter("ticket");
        if (StringUtils.isBlank(ticket)) {
            return ResultData.fail("ticket不能为空.");
        }
        // 应用代码
        String appCode = request.getParameter("appCode");
        if (StringUtils.isBlank(appCode)) {
            return ResultData.fail("appCode不能为空.");
        }
        // 域ID
        String domainID = request.getParameter("domain_id");
        if (StringUtils.isBlank(domainID)) {
            return ResultData.fail("domain_id不能为空.");
        }
        // 组织ID
        String orgId = request.getParameter("org_id");
        if (StringUtils.isBlank(orgId)) {
            return ResultData.fail("org_id不能为空.");
        }
        ClientDetail clientDetail = clientDetailService.getByAppCode(appCode);
        if (Objects.isNull(clientDetail)) {
            return ResultData.fail("应用[" + appCode + "]未授权.");
        }
        if (StringUtils.isBlank(workPlusApiHost)) {
            return ResultData.fail("workplus工作台API访问地址不能为空.");
        }
        //1.请求workplus的API，通过app的key和secret获取accessToken
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("grant_type", "client_credentials");
        paramMap.put("scope", "app");
        paramMap.put("domain_id", domainID);
        paramMap.put("org_id", orgId);
        paramMap.put("client_secret", clientDetail.getClientSecret());
        paramMap.put("client_id", clientDetail.getClientId());
        String url = workPlusApiHost + GET_TOKEN_URL;
        String paramString = JsonUtils.toJson(paramMap);
        LOG.debug("WorkPlus认证请求: {},参数: {}", url, paramString);
        Map<String, Object> resultMap;
        String token;
        try {
            String result = HttpUtils.sendPost(url, paramString);
            LOG.debug("WorkPlus认证请求结果: {}", result);
            if (StringUtils.isBlank(result)) {
                return ResultData.fail("WorkPlus认证失败,返回Token结果为空.");
            }
            resultMap = JsonUtils.fromJson(result, HashMap.class);
            if (Objects.isNull(resultMap)) {
                return ResultData.fail("WorkPlus认证失败,返回结果Map为空.");
            }
            token = String.valueOf(resultMap.get("access_token"));
            if (StringUtils.isBlank(token)) {
                return ResultData.fail("WorkPlus认证失败,返回Token为空.");
            }
        } catch (Exception e) {
            LOG.error("发起WorkPlus平台请求[" + url + "]异常.", e);
            return ResultData.fail("发起WorkPlus平台请求[" + url + "]异常.");
        }
        //2.请求workplus的API，验证APP传过来的ticket是否正确
        url = String.format(TICKET_CHECK_URL, ticket, token);
        LOG.debug("WorkPlus验证Ticket请求: {}", url);
        String userId;
        try {
            String result = HttpUtils.sendGet(url);
            LOG.debug("WorkPlus验证Ticket请求结果: {}", result);
            if (StringUtils.isBlank(result)) {
                return ResultData.fail("WorkPlus验证Ticket失败,返回结果为空.");
            }
            resultMap = JsonUtils.fromJson(result, HashMap.class);
            if (Objects.isNull(resultMap)) {
                return ResultData.fail("WorkPlus验证Ticket失败,返回结果Map为空.");
            }
            String status = String.valueOf(resultMap.get("status"));
            if (StringUtils.isBlank(status)) {
                return ResultData.fail("WorkPlus验证Ticket失败,返回Status为空.");
            }
            if (!Objects.equals("0", status)) {
                return ResultData.fail("WorkPlus验证Ticket失败,返回Status为: " + status);
            }
            userId = (String) ((Map)resultMap.get("result")).get("client_id");
            if (StringUtils.isBlank(userId)) {
                return ResultData.fail("WorkPlus验证Ticket失败,返回client_id为空.");
            }
        } catch (Exception e) {
            LOG.error("WorkPlus验证Ticket请求[" + url + "]异常.", e);
            return ResultData.fail("WorkPlus验证Ticket请求[" + url + "]异常.");
        }
        //3.获取用户信息
        url = String.format(GET_USER_INFO_URL, userId, token);
        LOG.debug("WorkPlus获取用户信息请求: {}", url);
        String userName;
        try {
            String result = HttpUtils.sendGet(url);
            LOG.debug("WorkPlus获取用户信息请求结果: {}", result);
            if (StringUtils.isBlank(result)) {
                return ResultData.fail("WorkPlus获取用户信息失败,返回结果为空.");
            }
            resultMap = JsonUtils.fromJson(result, HashMap.class);
            if (Objects.isNull(resultMap)) {
                return ResultData.fail("WorkPlus获取用户信息失败,返回结果Map为空.");
            }
            userName = (String) ((Map)resultMap.get("result")).get("username");
            if (StringUtils.isBlank(userName)) {
                return ResultData.fail("WorkPlus获取用户信息失败,返回userName为空.");
            }
        } catch (Exception e) {
            LOG.error("WorkPlus获取用户信息[" + url + "]异常.", e);
            return ResultData.fail("WorkPlus获取用户信息[" + url + "]异常.");
        }
        Account accountObj = accountService.getByAccountAndTenantCode(userName, clientDetail.getTenantCode());
        if (Objects.nonNull(accountObj)) {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setTenant(accountObj.getTenantCode());
            loginRequest.setAccount(accountObj.getAccount());
            loginRequest.setReqId(IdGenerator.uuid2());
            return login(loginRequest, accountObj);
        } else {
            LOG.error("账号[{}]不存在，单点登录失败.", userName);
            return ResultData.fail("账号[" + userName + "]不存在，单点登录失败.");
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
