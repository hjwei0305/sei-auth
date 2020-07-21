package com.changhong.sei.auth.event.listener;

import com.changhong.sei.auth.aop.LoginHistoryAspect;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.entity.LoginHistory;
import com.changhong.sei.auth.event.LoginEvent;
import com.changhong.sei.auth.service.LoginHistoryService;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.util.thread.ThreadLocalUtil;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.LocalDateTime;

/**
 * 登录事件监听，用于记录登录日志
 *
 * @author zuihou
 * @date 2020年03月18日17:39:59
 */
@EnableAsync
public class LoginListener {
    private static final Logger LOG = LoggerFactory.getLogger(LoginHistoryAspect.class);

    @Autowired
    private LoginHistoryService historyService;

    private final static String[] BROWSER = new String[] {
            "Chrome", "Firefox", "Microsoft Edge", "Safari", "Opera"
    };
    private final static String[] OPERATING_SYSTEM = new String[]{
            "Android", "Linux", "Mac OS X", "Ubuntu", "Windows 10", "Windows 8", "Windows 7", "Windows XP", "Windows Vista"
    };

    private static String simplifyOperatingSystem(String operatingSystem) {
        for (String b : OPERATING_SYSTEM) {
            if (StringUtils.containsIgnoreCase(operatingSystem, b)) {
                return b;
            }
        }
        return operatingSystem;
    }

    private static String simplifyBrowser(String browser) {
        for (String b : BROWSER) {
            if (StringUtils.containsIgnoreCase(browser, b)) {
                return b;
            }
        }
        return browser;
    }

    @Async
    @EventListener({LoginEvent.class})
    public void saveSysLog(LoginEvent event) {
        LoginRequest request = event.getLoginRequest();
        ResultData<SessionUserResponse> result = event.getLoginResult();

        LoginHistory history = new LoginHistory();
        history.setAccount(request.getAccount());
        history.setTenantCode(request.getTenant());
        history.setLoginDate(LocalDateTime.now());

        try {
            history.setLoginIp(ThreadLocalUtil.getTranVar("ClientIP"));
            String agent = ThreadLocalUtil.getTranVar("UserAgent");
            history.setLoginUserAgent(agent);

            //解析agent字符串
            UserAgent userAgent = UserAgent.parseUserAgentString(agent);
            //获取浏览器对象
            Browser browser = userAgent.getBrowser();
            //获取操作系统对象
            OperatingSystem operatingSystem = userAgent.getOperatingSystem();

            // 浏览器名
            history.setBrowser(simplifyBrowser(browser.getName()));
            // 操作系统名
            history.setOsName(simplifyOperatingSystem(operatingSystem.getName()));

            if (result.getSuccess()) {
                SessionUserResponse dto = result.getData();
                history.setLoginStatus(dto.getLoginStatus());
                history.setLoginLog(result.getMessage());

                // 记录登录错误次数
                if (!(SessionUserResponse.LoginStatus.success == dto.getLoginStatus()
                        || SessionUserResponse.LoginStatus.multiTenant == dto.getLoginStatus())) {
                    historyService.recordLoginFailureNum(request.getTenant(), request.getAccount(), request.getReqId());
                }
            } else {
                // 记录登录错误次数
                historyService.recordLoginFailureNum(request.getTenant(), request.getAccount(), request.getReqId());

                history.setLoginStatus(SessionUserResponse.LoginStatus.failure);
                history.setLoginLog(result.getMessage());
            }
            historyService.save(history);
        } catch (Exception e) {
            LOG.error(request.getAccount() + " -登录历史记录异常", e);
        }
    }
}
