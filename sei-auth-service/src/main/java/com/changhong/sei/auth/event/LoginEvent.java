package com.changhong.sei.auth.event;

import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.core.dto.ResultData;
import org.springframework.context.ApplicationEvent;

/**
 * 登录事件
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:57
 */
public class LoginEvent extends ApplicationEvent {
    private static final long serialVersionUID = -5561292125221426191L;

    private transient LoginRequest loginRequest;
    private transient ResultData<SessionUserResponse> loginResult;

    public LoginEvent(LoginRequest loginRequest, ResultData<SessionUserResponse> loginResult) {
        super("LoginEvent");
        this.loginRequest = loginRequest;
        this.loginResult = loginResult;
    }

    public LoginRequest getLoginRequest() {
        return loginRequest;
    }

    public ResultData<SessionUserResponse> getLoginResult() {
        return loginResult;
    }
}
