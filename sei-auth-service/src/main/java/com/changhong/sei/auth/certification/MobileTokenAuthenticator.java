package com.changhong.sei.auth.certification;

import com.changhong.sei.auth.dto.ChannelEnum;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.event.LoginEvent;
import com.changhong.sei.auth.service.ValidateCodeService;
import com.changhong.sei.core.context.ApplicationContextHolder;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.log.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 实现功能：手机登录认证TokenGranter
 * 手机号接收验证码认证登录
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:57
 */
@Component(TokenAuthenticator.AUTH_TYPE_MOBILE)
public class MobileTokenAuthenticator extends AbstractTokenAuthenticator implements TokenAuthenticator {

    @Autowired
    private ValidateCodeService validateCodeService;

    @Override
    public ResultData<SessionUserResponse> auth(LoginRequest loginRequest) {
        String reqId = loginRequest.getReqId();
        String verifyCode = loginRequest.getVerifyCode();
        String account = loginRequest.getAccount();

        // 认证码检查,登录错误指定次数后要求输入验证码
        ResultData<String> checkResult = validateCodeService.check(reqId, verifyCode);
        if (checkResult.failed()) {
            LogUtil.warn("认证失败.账号[{}]: {}", account, checkResult.getMessage());

            ResultData<SessionUserResponse> result = ResultData.success(checkResult.getMessage(), SessionUserResponse.build().setLoginStatus(SessionUserResponse.LoginStatus.captchaError));
            // 发布登录验证码错误事件
            ApplicationContextHolder.publishEvent(new LoginEvent(loginRequest, result));
            return result;
        }

        List<Account> accountList = accountService.findByOpenIdAndChannel(account, ChannelEnum.Mobile);
        if (Objects.isNull(accountList)) {
            LogUtil.warn("账号或密码错误.");

            ResultData<SessionUserResponse> result = ResultData.success(checkResult.getMessage(), SessionUserResponse.build().setLoginStatus(SessionUserResponse.LoginStatus.failure));
            // 发布登录验证码错误事件
            ApplicationContextHolder.publishEvent(new LoginEvent(loginRequest, result));
            return result;
        } else {
            if (accountList.size() == 1) {
                return login(loginRequest, accountList.get(0));
            } else {
                ResultData<SessionUserResponse> result = ResultData.success(checkResult.getMessage(), SessionUserResponse.build().setLoginStatus(SessionUserResponse.LoginStatus.multiTenant));
                // 发布登录验证码错误事件
                ApplicationContextHolder.publishEvent(new LoginEvent(loginRequest, result));
                return result;
            }
        }
    }

}
