package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.certification.TokenAuthenticatorBuilder;
import com.changhong.sei.auth.certification.sso.SingleSignOnAuthenticator;
import com.changhong.sei.auth.common.Constants;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.JsonUtils;
import com.changhong.sei.exception.WebException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * 实现功能： 单点登录
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-16 14:52
 */
@Controller
@Api(value = "SingleSignOnApi", tags = "单点登录服务")
public class SingleSignOnController implements Constants {
    private static final Logger LOG = LoggerFactory.getLogger(SingleSignOnController.class);

    @Autowired
    private TokenAuthenticatorBuilder builder;

    @ApiOperation(value = "微信授权路由", notes = "微信授权路由")
    @RequestMapping(AUTHORIZE_ENDPOINT)
    public String authorize(HttpServletRequest request) throws Exception {
        String authType = request.getParameter("authType");
        if (StringUtils.isBlank(authType)) {
            throw new WebException("单点登录失败：authType不能为空！");
        }
        SingleSignOnAuthenticator authenticator = builder.getSingleSignOnAuthenticator(authType);

        String endpoint = authenticator.getAuthorizeEndpoint(request);
        LOG.info("【微信网页授权】获取code, endpoint={}", endpoint);
        return "redirect:" + endpoint;
    }

    @ResponseBody
    @ApiOperation(value = "微信授权路由", notes = "微信授权路由")
    @RequestMapping("/sso/authorizeData")
    public ResultData<Map<String, String>> authorizeData(HttpServletRequest request) throws Exception {
        String authType = request.getParameter("authType");
        if (StringUtils.isBlank(authType)) {
            throw new WebException("单点登录失败：authType不能为空！");
        }
        SingleSignOnAuthenticator authenticator = builder.getSingleSignOnAuthenticator(authType);

        ResultData<Map<String, String>> result = authenticator.getAuthorizeData(request);
        LOG.info("【微信网页授权】获取code, result = {}", JsonUtils.toJson(result));
        return result;
    }

    @ApiOperation(value = "单点登录", notes = "PC应用单点登录")
    @RequestMapping(value = {SSO_LOGIN_ENDPOINT})
    public Object ssoLogin(HttpServletRequest request) {
        String authType = request.getParameter("authType");
        if (StringUtils.isBlank(authType)) {
            throw new WebException("单点登录失败：authType不能为空！");
        }
        SingleSignOnAuthenticator authenticator = builder.getSingleSignOnAuthenticator(authType);

        // 单点登录地址
        String loginUrl = authenticator.getLogoutUrl();
        ResultData<SessionUserResponse> result = authenticator.auth(request);
        LOG.info("单点登录验证结果：{}", result);
        if (result.getSuccess()) {
            SessionUserResponse userResponse = result.getData();
            if (SessionUserResponse.LoginStatus.success == userResponse.getLoginStatus()) {
                return redirectMainPage(userResponse.getSessionId(), authType);
            } else {
                if (StringUtils.isNotBlank(userResponse.getOpenId())) {
                    // 账号绑定页面
                    loginUrl = authenticator.getWebBaseUrl() + "/#/sso/socialAccount?authType=" + authType
                            + "&tenant=" + (StringUtils.isNotBlank(userResponse.getTenantCode()) ? userResponse.getTenantCode() : "")
                            + "&openId=" + (StringUtils.isNotBlank(userResponse.getOpenId()) ? userResponse.getOpenId() : "");
                    LOG.error("单点登录失败：需要绑定平台账号！");
                }
            }
        }
        LOG.error("单点登录失败：未获取到当前登录用户！");
        return "redirect:" + loginUrl;
    }

    //    @ApiOperation(value = "跳转地址", notes = "单点登录跳转地址")
//    @RequestMapping(value = "/sso/redirectMainPage")
//    public String redirectMainPage(@RequestParam("sid") String sid, @RequestParam("authType") String authType) {
    private String redirectMainPage(String sid, String authType) {
        SingleSignOnAuthenticator authenticator = builder.getSingleSignOnAuthenticator(authType);

        // 跳转到新版(react)的页面
        String url = authenticator.getIndexUrl();
        if (StringUtils.isBlank(url)) {
            url = authenticator.getWebBaseUrl() + "/#/sso/ssoWrapperPage?sid=" + sid;
            LOG.info("单点登录跳转地址: {}", url);
            return "redirect:" + url;
        }
        //单点错误页面
        return "redirect:" + url;
    }

    @ResponseBody
    @ApiOperation(value = "绑定社交账号", notes = "绑定社交账号")
    @RequestMapping(value = "/sso/binding/socialAccount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultData<SessionUserResponse> binding(@RequestBody @Valid LoginRequest loginRequest) {
        return builder.getSingleSignOnAuthenticator(loginRequest.getAuthType()).bindingAccount(loginRequest);
    }

    @ResponseBody
    @ApiOperation(value = "绑定社交账号", notes = "绑定社交账号")
    @RequestMapping(value = "/sso/js/sdk", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultData<Map<String, String>> jsSdk(HttpServletRequest request) {
        String authType = request.getParameter("authType");
        if (StringUtils.isBlank(authType)) {
            throw new WebException("单点登录失败：authType不能为空！");
        }
        SingleSignOnAuthenticator authenticator = builder.getSingleSignOnAuthenticator(authType);
        return authenticator.jsapi_ticket();
    }
}
