package com.changhong.sei.auth.service;

import com.changhong.sei.auth.certification.TokenAuthenticatorBuilder;
import com.changhong.sei.auth.certification.sso.SingleSignOnAuthenticator;
import com.changhong.sei.auth.controller.SingleSignOnController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * *************************************************************************************************<br>
 * <br>
 * 实现功能：单点登录服务 <br>
 * <br>
 * ------------------------------------------------------------------------------------------------<br>
 * 版本          变更时间             变更人                     变更原因<br>
 * ------------------------------------------------------------------------------------------------<br>
 * 1.0.00      2020/5/23 15:31      冯华(冯华)                新建<br>
 * *************************************************************************************************<br>
 */
@Service
public class SingleSignOnService {
    @Autowired
    private TokenAuthenticatorBuilder builder;

    private static final Logger LOG = LoggerFactory.getLogger(SingleSignOnService.class);

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

    /**
     * 获取登录后的跳转地址
     * @param sid 会话id
     * @param request
     * @return
     */
    public String redirectMainPage(String sid,  HttpServletRequest request) {
        //浏览器客户端信息
        String ua = request.getHeader("User-Agent");
        String authType = request.getParameter("authType");
        //客户端是否是pc
        boolean pcAgent = true;
        if (checkAgentIsMobile(ua)) {
            pcAgent = false;
            request.setAttribute("LoginType", "APP");
        } else {
            request.setAttribute("LoginType", "SSO");
        }
        SingleSignOnAuthenticator authenticator = builder.getSingleSignOnAuthenticator(authType);
        String url = authenticator.getIndexUrl();
        if (pcAgent) {
            // PC登录：跳转到新版(react)的页面
            if (StringUtils.isBlank(url)) {
                url = authenticator.getWebBaseUrl() + "/#/sso/ssoWrapperPage?sid=" + sid;
                LOG.error("单点登录跳转地址: {}", url);
            }
        } else {
            // APP：跳转到移动端
            if (StringUtils.isBlank(url)) {
                url = authenticator.getAppBaseUrl() + "/#/main?sid=" + sid;
                LOG.error("单点登录跳转地址: {}", url);
            }
        }
        //单点错误页面
        return  url;
    }
}
