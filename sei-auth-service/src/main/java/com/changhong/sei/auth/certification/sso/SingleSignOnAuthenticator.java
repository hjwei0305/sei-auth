package com.changhong.sei.auth.certification.sso;

import com.changhong.sei.auth.certification.TokenAuthenticator;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.log.LogUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-23 12:16
 */
public interface SingleSignOnAuthenticator extends TokenAuthenticator {

    /**
     * 前端web根url地址.必须
     * 如:http://tsei.changhong.com:8090/sei-portal-web
     */
    String getWebBaseUrl();

    /**
     * APP根url地址.必须
     * 如:http://tsei.changhong.com:8090/sei-app
     */
    String getAppBaseUrl();

    /**
     * 登录成功url地址
     */
    default String getIndexUrl(SessionUserResponse userResponse) {
        // PC登录：跳转到新版(react)的页面
        String url = getWebBaseUrl() + "/#/sso/ssoWrapperPage?sid=" + userResponse.getSessionId();
        LogUtil.error("单点登录跳转地址: {}", url);
        return url;
    }

    /**
     * 登录失败url地址
     */
    String getLogoutUrl();

    /**
     * 获取用户信息
     */
    ResultData<SessionUserResponse> auth(HttpServletRequest request);

}
