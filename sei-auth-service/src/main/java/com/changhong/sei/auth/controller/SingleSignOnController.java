package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.certification.TokenAuthenticatorBuilder;
import com.changhong.sei.auth.certification.sso.Oauth2Authenticator;
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
    @RequestMapping(path = AUTHORIZE_ENDPOINT, method = {RequestMethod.GET, RequestMethod.POST})
    public String authorize(HttpServletRequest request) {
        String authType = request.getParameter("authType");
        if (StringUtils.isBlank(authType)) {
            throw new WebException("单点登录失败：authType不能为空！");
        }
        Oauth2Authenticator authenticator = builder.getOauth2Authenticator(authType);

        String endpoint = authenticator.getAuthorizeEndpoint(request);
        LOG.info("【微信网页授权】获取code, endpoint={}", endpoint);
        return "redirect:" + endpoint;
    }

    @ResponseBody
    @ApiOperation(value = "微信授权路由", notes = "微信授权路由")
    @RequestMapping(path = "/sso/authorizeData", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultData<Map<String, String>> authorizeData(HttpServletRequest request) {
        String authType = request.getParameter("authType");
        if (StringUtils.isBlank(authType)) {
            throw new WebException("单点登录失败：authType不能为空！");
        }
        Oauth2Authenticator authenticator = builder.getOauth2Authenticator(authType);

        ResultData<Map<String, String>> result = authenticator.getAuthorizeData(request);
        LOG.info("【微信网页授权】获取code, result = {}", JsonUtils.toJson(result));
        return result;
    }

    @ApiOperation(value = "单点登录", notes = "PC应用单点登录")
    @RequestMapping(path = {SSO_LOGIN_ENDPOINT}, method = {RequestMethod.GET, RequestMethod.POST})
    public String ssoLogin(HttpServletRequest request) {
        String authType = request.getParameter("authType");
        if (StringUtils.isBlank(authType)) {
            throw new WebException("单点登录失败：authType不能为空！");
        }
        SingleSignOnAuthenticator authenticator = builder.getSingleSignOnAuthenticator(authType);

        //浏览器客户端信息
        String ua = request.getHeader("User-Agent");
        //客户端是否是pc
        if (authenticator.checkAgentIsMobile(ua)) {
            request.setAttribute("LoginType", "APP");
        } else {
            request.setAttribute("LoginType", "SSO");
        }

        // 单点登录地址
        String index = authenticator.getLogoutUrl();
        ResultData<SessionUserResponse> result = authenticator.auth(request);
        LOG.error("单点登录验证结果：{}", result);
        if (result.getSuccess()) {
            SessionUserResponse userResponse = result.getData();
            if (SessionUserResponse.LoginStatus.success == userResponse.getLoginStatus()) {
                index = authenticator.getIndexUrl(userResponse);
            } else {
                index = authenticator.getIndexUrl(userResponse);
            }
        }
        LOG.error("单点登录失败：未获取到当前登录用户！");
        return "redirect:" + index;
    }

    @ResponseBody
    @ApiOperation(value = "绑定社交账号", notes = "绑定社交账号")
    @RequestMapping(path = "/sso/binding/socialAccount", method = {RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultData<SessionUserResponse> binding(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest request) {
        return builder.getOauth2Authenticator(loginRequest.getAuthType()).bindingAccount(loginRequest, request);
    }

    @ResponseBody
    @ApiOperation(value = "绑定社交账号", notes = "绑定社交账号")
    @RequestMapping(path = "/sso/js/sdk", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultData<Map<String, String>> jsSdk(HttpServletRequest request) {
        String authType = request.getParameter("authType");
        if (StringUtils.isBlank(authType)) {
            throw new WebException("单点登录失败：authType不能为空！");
        }
        Oauth2Authenticator authenticator = builder.getOauth2Authenticator(authType);
        return authenticator.jsapi_ticket();
    }
}
