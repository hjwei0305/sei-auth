package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.certification.sso.SingleSignOnAuthenticator;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.exception.WebException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * 实现功能： 单点登录
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-16 14:52
 */
@Controller
@Api(value = "SingleSignOnApi", tags = "单点登录服务")
public class SingleSignOnController {
    private static final Logger LOG = LoggerFactory.getLogger(SingleSignOnController.class);

    private static final String AUTHORIZE_URI = "/sso/authorize";
    private static final String SSO_LOGIN_URI = "/sso/login";

    @Autowired(required = false)
    private SingleSignOnAuthenticator authenticator;

    @ApiOperation(value = "微信授权路由", notes = "微信授权路由")
    @RequestMapping(AUTHORIZE_URI)
    public String authorize(HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*
        http://tsei.changhong.com:8090/api-gateway/sei-auth/sso/login
        https://open.weixin.qq.com/connect/oauth2/authorize?appid=wwdc99e9511ccac381&redirect_uri=http%3A%2F%2Ftsei.changhong.com%3A8090%2Fapi-gateway%2Fsei-auth%2Fsso%2Flogin&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect
         */
        //这个方法的三个参数分别是授权后的重定向url、获取用户信息类型和state
        String redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect";
        String corpid = "";
        // 微信定义的一个参数，用户可以传入自定义的参数
        String state = "sei";
        // 用户微信授权登录后重定向的页面路由
        String redirect_uri = request.getRequestURL().toString().replace(AUTHORIZE_URI, SSO_LOGIN_URI);
        redirectUrl = String.format(redirectUrl, corpid, URLEncoder.encode(redirect_uri, "UTF-8"), state);

        LOG.info("【微信网页授权】获取code,redirectUrl={}", redirectUrl);
        return "redirect:" + redirectUrl;
    }

    @ApiOperation(value = "单点登录", notes = "PC应用单点登录")
    @RequestMapping(value = {SSO_LOGIN_URI})
    public Object ssoLogin(HttpServletRequest request, HttpServletResponse response) {
        if (Objects.isNull(authenticator)) {
            throw new WebException("单点登录失败：未单点登录配置[sei.sso]不正确！");
        }
        ResultData<SessionUserResponse> result = authenticator.auth(request, response);
        if (result.getSuccess()) {
            SessionUserResponse userResponse = result.getData();
            if (SessionUserResponse.LoginStatus.success == userResponse.getLoginStatus()) {
                return redirectMainPage(userResponse.getSessionId());
            } else {
                // 单点登录地址
                String loginUrl = authenticator.getLogoutUrl();
                if (StringUtils.isNotBlank(userResponse.getOpenId())) {
                    loginUrl = loginUrl + "?tenant=" + (StringUtils.isNotBlank(userResponse.getTenantCode()) ? userResponse.getTenantCode() : "");
                    loginUrl = loginUrl + "&openId=" + (StringUtils.isNotBlank(userResponse.getOpenId()) ? userResponse.getOpenId() : "");
                }
                LOG.error("单点登录失败：未获取到当前登录用户！");
                return "redirect:" + loginUrl;
            }
        } else {
            // 单点登录地址
            String loginUrl = authenticator.getLogoutUrl();
            LOG.error("单点登录失败：未获取到当前登录用户！");
            return "redirect:" + loginUrl;
        }
    }

    @ApiOperation(value = "跳转地址", notes = "单点登录跳转地址")
    @RequestMapping(value = "/sso/redirectMainPage")
    public String redirectMainPage(@RequestParam("sid") String sid) {
        String url;
//        url = request.getParameter("url");
//        if (StringUtils.isNotBlank(url)) {
//            if (url.contains("?")) {
//                url = url + "&_s=" + sid;
//            } else {
//                url = url + "?_s=" + sid;
//            }
//            return "redirect:" + url;
//        } else {
        // 跳转到新版(react)的页面
        url = authenticator.getIndexUrl();
        if (StringUtils.isNotBlank(url)) {
            url = url + "SsoWrapperPage?_s=" + sid;
            return "redirect:" + url;
        }
//        }
        //单点错误页面
        return "redirect:/index";
    }

    @ResponseBody
    @ApiOperation(value = "绑定社交账号", notes = "绑定社交账号")
    @RequestMapping(value = "/sso/binding/socialAccount", method = RequestMethod.POST)
    public Object binding(LoginRequest loginRequest) {
        return authenticator.bindingAccount(loginRequest);
    }
}
