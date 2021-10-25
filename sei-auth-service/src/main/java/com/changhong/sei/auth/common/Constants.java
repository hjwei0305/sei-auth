package com.changhong.sei.auth.common;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-02 00:59
 */
public interface Constants {

    String COOKIE_SID = "x-sid";
    String COOKIE_CLIENT = "SEI_CLIENT";

    /**
     * 会话认证缓存key
     */
    String REDIS_KEY_PREFIX = "sei:auth:login:";
    /**
     * 认证码缓存key
     */
    String VERIFY_CODE_KEY = "sei:auth:verify_code:";
    /**
     * 半小时内登陆错误次数
     */
    String LOGIN_NUM_KEY = "sei:auth:login:num:";
    /**
     * OAuth2授权码缓存key
     */
    String OAUTH2_CODE_KEY = "sei:auth:verify_code:";

    /**
     * oauth2授权端点
     */
    String AUTHORIZE_ENDPOINT = "/sso/authorize";
    /**
     * 单点登录端点
     */
    String SSO_LOGIN_ENDPOINT = "/sso/login";

    /**
     * 所有API接口
     *
     * @author kong
     */
    final class OAuth2Api {
        public static String authorize = "authorize";
        public static String token = "token";
        public static String refresh = "refresh";
        public static String client_token = "client_token";
        public static String doLogin = "doLogin";
        public static String doConfirm = "doConfirm";
    }

    /**
     * OAuth2所有参数名称
     */
    final class OAuth2Param {
        /**
         * 返回类型[授权码:code 隐藏式:token 密码式:password 客户端凭证:client_credentials]
         */
        public static String response_type = "response_type";
        /**
         * 应用标示
         */
        public static String client_id = "client_id";
        public static String client_secret = "client_secret";
        /**
         * 授权后重定向的回调链接地址，请使用urlencode对链接进行处理
         */
        public static String redirect_uri = "redirect_uri";
        /**
         * 应用授权作用域
         */
        public static String scope = "scope";
        /**
         * 重定向后会带上state参数，可以填写a-zA-Z0-9的参数值，长度不可超过128个字节
         */
        public static String state = "state";
        /**
         * 临时授权码
         */
        public static String code = "code";
        /**
         * token
         */
        public static String token = "token";
        public static String access_token = "access_token";
        public static String refresh_token = "refresh_token";
        public static String grant_type = "grant_type";
        public static String userId = "user_id";
        public static String username = "username";
        public static String password = "password";
    }

    /**
     * OAuth2所有返回类型
     */
    final class OAuth2ResponseType {
        /**
         * 授权码
         */
        public static String code = "code";
        /**
         * 隐藏式(简化)
         */
        public static String token = "token";
    }

    /**
     * OAuth2所有授权类型
     */
    final class OAuth2GrantType {
        public static String authorization_code = "authorization_code";
        public static String refresh_token = "refresh_token";
        public static String password = "password";
        public static String client_credentials = "client_credentials";
    }
}
