package com.changhong.sei.auth.aop;

import com.changhong.sei.auth.common.Constants;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.entity.LoginHistory;
import com.changhong.sei.auth.service.LoginHistoryService;
import com.changhong.sei.core.cache.CacheBuilder;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.HttpUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(LoginHistoryAspect.class);

    @Autowired
    private LoginHistoryService historyService;

    /**
     * 拦截@see com.changhong.sei.auth.service.AuthenticationServiceImpl#login 方法的返回,记录登录历史
     */
    @AfterReturning(value = "execution(* com.changhong.sei.auth.controller.AuthenticationController.login(..))", argNames = "joinPoint, result", returning = "result")
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

                if (result.getSuccess()) {
                    SessionUserResponse dto = result.getData();
                    history.setLoginStatus(dto.getLoginStatus());
                    history.setLoginLog(result.getMessage());

                    // 记录登录错误次数
                    if (!(SessionUserResponse.LoginStatus.success == dto.getLoginStatus()
                            || SessionUserResponse.LoginStatus.multiTenant == dto.getLoginStatus())) {
                        historyService.recordLoginFailureNum(loginRequest.getTenant(), loginRequest.getAccount());
                    }
                } else {
                    // 记录登录错误次数
                    historyService.recordLoginFailureNum(loginRequest.getTenant(), loginRequest.getAccount());

                    history.setLoginStatus(SessionUserResponse.LoginStatus.failure);
                    history.setLoginLog(result.getMessage());
                }
                historyService.save(history);
            } catch (Exception e) {
                LOG.error(loginRequest.getAccount() + " -登录历史记录异常", e);
            }
        }
    }


}
