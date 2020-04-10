package com.changhong.sei.auth.certification;

import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.core.dto.ResultData;
import org.springframework.stereotype.Component;

/**
 * 实现功能：账号密码登录获取token
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:57
 */
@Component(TokenAuthenticator.AUTH_TYPE_PASSWORD)
public class PasswordTokenAuthenticator extends AbstractTokenAuthenticator implements TokenAuthenticator {

    @Override
    public ResultData<SessionUserResponse> auth(LoginRequest loginRequest) {
        return login(loginRequest);
    }
}
