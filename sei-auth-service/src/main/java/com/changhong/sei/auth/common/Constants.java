package com.changhong.sei.auth.common;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-02 00:59
 */
public interface Constants {
    /**
     * 认证码缓存key
     */
    String VERIFY_CODE_KEY = "sei:auth:login:verify_code:";
    /**
     * 半小时内登陆错误次数
     */
    String LOGIN_NUM_KEY = "sei:auth:login:num:";

    /**
     * oauth2授权端点
     */
    String AUTHORIZE_ENDPOINT = "/sso/authorize";
    /**
     * 单点登录端点
     */
    String SSO_LOGIN_ENDPOINT = "/sso/login";
}
