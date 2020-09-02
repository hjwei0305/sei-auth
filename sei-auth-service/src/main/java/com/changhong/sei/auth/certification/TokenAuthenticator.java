package com.changhong.sei.auth.certification;

import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.log.LogUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 实现功能：授权认证统一接口
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:57
 */
public interface TokenAuthenticator {

    /**
     * 验证码+账号密码认证
     */
    String AUTH_TYPE_CAPTCHA = "captcha";
    /**
     * 账号密码认证
     */
    String AUTH_TYPE_PASSWORD = "password";
    /**
     * 微信认证
     */
    String AUTH_TYPE_WE_CHAT = "weChat";
    /**
     * 长虹GT单点认证
     */
    String AUTH_TYPE_CH_GT = "chGT";


    String[] AGENT = {"iphone", "android", "ipad", "phone", "mobile", "wap", "netfront", "java", "operamobi",
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
     */
    default boolean checkAgentIsMobile(String ua) {
        LogUtil.error("User-Agent的类型为:" + ua);
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
     * 获取用户信息
     *
     * @param loginParam 授权参数
     * @return LoginDTO
     */
    ResultData<SessionUserResponse> auth(LoginRequest loginParam);

}
