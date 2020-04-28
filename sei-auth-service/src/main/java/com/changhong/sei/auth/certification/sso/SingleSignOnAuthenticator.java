package com.changhong.sei.auth.certification.sso;

import com.changhong.sei.auth.certification.TokenAuthenticator;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.core.dto.ResultData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-23 12:16
 */
public interface SingleSignOnAuthenticator extends TokenAuthenticator {

    /**
     * 登录成功url地址
     */
    String getIndexUrl();

    /**
     * 登录失败url地址
     */
    String getLogoutUrl();

    /**
     * 绑定账号
     */
    ResultData<String> bindingAccount(LoginRequest loginRequest);

    /**
     * 获取用户信息
     */
    ResultData<SessionUserResponse> auth(HttpServletRequest request, HttpServletResponse response);
}
