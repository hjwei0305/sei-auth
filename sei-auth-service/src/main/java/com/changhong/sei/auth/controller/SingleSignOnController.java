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

    private final static String[] AGENT = {"iphone", "android", "ipad", "phone", "mobile", "wap", "netfront", "java", "operamobi",
            "operamini", "ucweb", "windowsce", "symbian", "series", "webos", "sony", "blackberry", "dopod",
            "nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
            "docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",
            "techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem",
            "wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
            "pantech", "gionee", "portalmmm", "jigbrowser", "hiptop", "benq", "haier", "^lct", "320x320",
            "240x320", "176x220", "w3c", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
            "blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
            "kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
            "mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
            "prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
            "smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
            "voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
            "googlebot-mobile"};

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
        //浏览器客户端信息
        String ua = request.getHeader("User-Agent");
        //客户端是否是pc
        boolean pcAgent = true;
        if (checkAgentIsMobile(ua)) {
            pcAgent = false;
            request.setAttribute("LoginType", "APP");
        } else {
            request.setAttribute("LoginType", "SSO");
        }
        // 单点登录地址
        String loginUrl = authenticator.getLogoutUrl();
        ResultData<SessionUserResponse> result = authenticator.auth(request);
        LOG.error("单点登录验证结果：{}", result);
        if (result.getSuccess()) {
            SessionUserResponse userResponse = result.getData();
            if (SessionUserResponse.LoginStatus.success == userResponse.getLoginStatus()) {
                return redirectMainPage(userResponse.getSessionId(), authType, pcAgent);
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
    private String redirectMainPage(String sid, String authType, boolean pcAgent) {
        SingleSignOnAuthenticator authenticator = builder.getSingleSignOnAuthenticator(authType);
        String url = authenticator.getIndexUrl();
        if (pcAgent) {
            // PC登录：跳转到新版(react)的页面
            if (StringUtils.isBlank(url)) {
                url = authenticator.getWebBaseUrl() + "/#/sso/ssoWrapperPage?sid=" + sid;
                LOG.error("单点登录跳转地址: {}", url);
                return "redirect:" + url;
            }
        } else {
            // APP：跳转到移动端
            if (StringUtils.isBlank(url)) {
                url = authenticator.getAppBaseUrl() + "/index.html#/main?sid=" + sid;
                LOG.error("单点登录跳转地址: {}", url);
                return "redirect:" + url;
            }
        }
        //单点错误页面
        return "redirect:" + url;
    }

    /**
     * 判断User-Agent 是不是来自于手机
     *
     * @param ua
     * @return
     */
    public static boolean checkAgentIsMobile(String ua) {
        LOG.error("User-Agent的类型为:" + ua);
        if (ua != null) {
            ua = ua.toLowerCase();
            // 排除 苹果桌面系统
            if (!ua.contains("windows nt") && !ua.contains("macintosh")) {
                //移动端
                return StringUtils.containsAny(ua, AGENT);
            }
        }
        return false;
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
