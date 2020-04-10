package com.changhong.sei.auth.certification;

import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.core.dto.ResultData;

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
     * 获取用户信息
     *
     * @param loginParam 授权参数
     * @return LoginDTO
     */
    ResultData<SessionUserResponse> auth(LoginRequest loginParam);

}
