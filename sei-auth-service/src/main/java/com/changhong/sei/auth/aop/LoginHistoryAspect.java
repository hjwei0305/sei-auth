package com.changhong.sei.auth.aop;

import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.entity.LoginHistory;
import com.changhong.sei.auth.service.LoginHistoryService;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.HttpUtils;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
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
//@Aspect
//@Component
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
                history.setLoginIp(HttpUtils.getClientIP(req));
                String agent = req.getHeader("user-agent");
                history.setLoginUserAgent(agent);

                //解析agent字符串
                UserAgent userAgent = UserAgent.parseUserAgentString(agent);
                //获取浏览器对象
                Browser browser = userAgent.getBrowser();
                //获取操作系统对象
                OperatingSystem operatingSystem = userAgent.getOperatingSystem();

                System.out.println("浏览器名:"+browser.getName());
                System.out.println("浏览器类型:"+browser.getBrowserType());
                System.out.println("浏览器家族:"+browser.getGroup());
                System.out.println("浏览器生产厂商:"+browser.getManufacturer());
                System.out.println("浏览器使用的渲染引擎:"+browser.getRenderingEngine());
                System.out.println("浏览器版本:"+userAgent.getBrowserVersion());

                System.out.println("操作系统名:"+operatingSystem.getName());
                System.out.println("访问设备类型:"+operatingSystem.getDeviceType());
                System.out.println("操作系统家族:"+operatingSystem.getGroup());
                System.out.println("操作系统生产厂商:"+operatingSystem.getManufacturer());

                if (result.getSuccess()) {
                    SessionUserResponse dto = result.getData();
                    history.setLoginStatus(dto.getLoginStatus());
                    history.setLoginLog(result.getMessage());

                    // 记录登录错误次数
                    if (!(SessionUserResponse.LoginStatus.success == dto.getLoginStatus()
                            || SessionUserResponse.LoginStatus.multiTenant == dto.getLoginStatus())) {
                        historyService.recordLoginFailureNum(loginRequest.getTenant(), loginRequest.getAccount(), loginRequest.getReqId());
                    }
                } else {
                    // 记录登录错误次数
                    historyService.recordLoginFailureNum(loginRequest.getTenant(), loginRequest.getAccount(), loginRequest.getReqId());

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
