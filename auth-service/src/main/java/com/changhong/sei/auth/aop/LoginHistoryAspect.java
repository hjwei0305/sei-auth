package com.changhong.sei.auth.aop;

import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.entity.LoginHistory;
import com.changhong.sei.auth.manager.LoginHistoryManager;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.util.HttpUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:57
 */
@Aspect
@Component
public class LoginHistoryAspect {

    @Autowired
    private LoginHistoryManager historyManager;

    /**
     * 拦截@see com.changhong.sei.auth.service.AuthenticationServiceImpl#login 方法的返回,记录登录历史
     */
    @AfterReturning(value = "execution(* com.changhong.sei.auth.service.AuthenticationServiceImpl.login(..))", argNames = "joinPoint, result", returning = "result")
    public void afterReturning(JoinPoint joinPoint, ResultData<SessionUserResponse> result) {
        Object[] args = joinPoint.getArgs();
        if (Objects.nonNull(args) && args.length == 1) {
            LoginRequest loginRequest = (LoginRequest) args[0];

            LoginHistory history = new LoginHistory();
            history.setAccount(loginRequest.getAccount());
            history.setTenantCode(loginRequest.getTenant());
            history.setLoginDate(LocalDateTime.now());
            try {
                HttpServletRequest req = HttpUtils.getRequest();
                history.setLoginIp(HttpUtils.getIpAddr(req));
                history.setLoginUserAgent(req.getHeader("user-agent"));

                if (result.getSuccessful()) {
                    SessionUserResponse dto = result.getData();
                    history.setLoginStatus(dto.getLoginStatus());
                    history.setLoginLog(result.getMessage());
                } else {
                    history.setLoginStatus(SessionUserResponse.LoginStatus.failure);
                    history.setLoginLog(result.getMessage());
                }
                historyManager.save(history);
            } catch (Exception e) {
                LogUtil.error("", e);
            }
        }
    }
}
