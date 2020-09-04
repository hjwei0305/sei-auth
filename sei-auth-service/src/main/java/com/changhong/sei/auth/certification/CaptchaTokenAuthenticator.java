package com.changhong.sei.auth.certification;

import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.event.LoginEvent;
import com.changhong.sei.auth.service.LoginHistoryService;
import com.changhong.sei.auth.service.ValidateCodeService;
import com.changhong.sei.core.context.ApplicationContextHolder;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.log.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 实现功能：验证码TokenGranter
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:57
 */
@Component(TokenAuthenticator.AUTH_TYPE_CAPTCHA)
public class CaptchaTokenAuthenticator extends AbstractTokenAuthenticator implements TokenAuthenticator {

    @Autowired
    private ValidateCodeService validateCodeService;
    @Autowired
    private LoginHistoryService historyService;

    @Override
    public ResultData<SessionUserResponse> auth(LoginRequest loginRequest) {
        String tenant = loginRequest.getTenant();
        String account = loginRequest.getAccount();

        // 认证码检查,登录错误指定次数后要求输入验证码
        ResultData<String> checkLoginData = historyService.checkLoginFailureNum(tenant, account);
        if (checkLoginData.failed()) {
            ResultData<String> checkResult = validateCodeService.check(loginRequest.getReqId(), loginRequest.getVerifyCode());
            if (checkResult.failed()) {
                LogUtil.warn("需输入验证码租户[{}]账号[{}]: {}", tenant, account, checkResult.getMessage());

                ResultData<SessionUserResponse> result = ResultData.success(checkResult.getMessage(), SessionUserResponse.build().setLoginStatus(SessionUserResponse.LoginStatus.captchaError));
                // 发布登录验证码错误事件
                ApplicationContextHolder.publishEvent(new LoginEvent(loginRequest, result));
                return result;
            }
        }

        return login(loginRequest);
    }

}
