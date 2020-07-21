package com.changhong.sei.auth.certification;

import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.event.LoginEvent;
import com.changhong.sei.auth.service.AccountService;
import com.changhong.sei.auth.service.SessionService;
import com.changhong.sei.core.context.ApplicationContextHolder;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.util.thread.ThreadLocalUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * 实现功能：验证码TokenGranter
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:57
 */
public abstract class AbstractTokenAuthenticator implements TokenAuthenticator {
    @Autowired
    protected AccountService accountService;
    @Autowired
    private SessionService sessionService;

    /**
     * 处理登录逻辑
     *
     * @param loginRequest 登录参数
     * @return 认证信息
     */
    protected ResultData<SessionUserResponse> login(LoginRequest loginRequest) {
        ResultData<SessionUserResponse> result;

        String tenant = loginRequest.getTenant();
        String account = loginRequest.getAccount();
        String password = loginRequest.getPassword();

        Account entity;
        if (StringUtils.isBlank(tenant)) {
            List<Account> accounts = accountService.getByAccount(account);
            if (CollectionUtils.isEmpty(accounts)) {
                result = ResultData.fail("账号密码错误,认证失败!");
                // 发布登录账号密码错误事件
                ApplicationContextHolder.publishEvent(new LoginEvent(loginRequest, result));
                return result;
            }

            if (accounts.size() > 1) {
                result = ResultData.success("请指定租户代码", SessionUserResponse.build().setLoginStatus(SessionUserResponse.LoginStatus.multiTenant));
                // 发布登录多租户事件
                // ApplicationContextHolder.publishEvent(new LoginEvent(loginRequest, result));
                return result;
            }
            entity = accounts.get(0);
        } else {
            entity = accountService.getByAccountAndTenantCode(account, tenant);
        }

        if (Objects.isNull(entity)) {
            result = ResultData.fail("账号密码错误,认证失败!");
            // 发布登录账号密码错误事件
            ApplicationContextHolder.publishEvent(new LoginEvent(loginRequest, result));
            return result;
        }

        // 验证密码
        if (!accountService.verifyPassword(password, entity.getPassword())) {
            result = ResultData.fail("账号密码错误,认证失败!");
            // 发布登录账号密码错误事件
            ApplicationContextHolder.publishEvent(new LoginEvent(loginRequest, result));
            return result;
        }
        // 密码过期时间
        LocalDate passwordExpire = entity.getPasswordExpireTime();
        if (Objects.nonNull(passwordExpire)) {
            // 密码过期
            if (passwordExpire.isBefore(LocalDate.now())) {
                SessionUserResponse userResponse = SessionUserResponse.build();
                userResponse.setTenantCode(entity.getTenantCode());
                userResponse.setAccount(entity.getAccount());
                userResponse.setLoginStatus(SessionUserResponse.LoginStatus.passwordExpire);
                result = ResultData.success("密码已过期,认证失败!", userResponse);
                // 发布登录账号已过期事件
                ApplicationContextHolder.publishEvent(new LoginEvent(loginRequest, result));
                return result;
            }
        }

        return login(loginRequest, entity);
    }

    protected ResultData<SessionUserResponse> login(LoginRequest loginRequest, Account entity) {
        ResultData<SessionUserResponse> result;
        // 检查是否被锁定
        if (entity.getLocked()) {
            result = ResultData.success("账号被锁定,认证失败!", SessionUserResponse.build().setLoginStatus(SessionUserResponse.LoginStatus.locked));
            // 发布登录账号被锁定事件
            ApplicationContextHolder.publishEvent(new LoginEvent(loginRequest, result));
            return result;
        }
        // 检查是否被冻结
        if (entity.getFrozen()) {
            result = ResultData.success("账号被冻结,认证失败!", SessionUserResponse.build().setLoginStatus(SessionUserResponse.LoginStatus.frozen));
            // 发布登录账号被冻结事件
            ApplicationContextHolder.publishEvent(new LoginEvent(loginRequest, result));
            return result;
        }
        // 检查账户是否在有效期内
        LocalDate validityDate = entity.getAccountExpired();
        if (Objects.nonNull(validityDate)) {
            if (validityDate.isBefore(LocalDate.now())) {
                result = ResultData.success("账号已过期,认证失败!", SessionUserResponse.build().setLoginStatus(SessionUserResponse.LoginStatus.expire));
                // 发布登录账号已过期事件
                ApplicationContextHolder.publishEvent(new LoginEvent(loginRequest, result));
                return result;
            }
        }

        // 客户端ip
        String ipAddr = ThreadLocalUtil.getTranVar("ClientIP");
        ResultData<SessionUser> resultData = accountService.getSessionUser(entity, ipAddr, loginRequest.getLocale());
        if (resultData.successful()) {
            SessionUser sessionUser = resultData.getData();

            // 生产token
            ContextUtil.generateToken(sessionUser);

            SessionUserResponse dto = SessionUserResponse.build().setLoginStatus(SessionUserResponse.LoginStatus.success);
            dto.setSessionId(sessionUser.getSessionId());
            dto.setTenantCode(sessionUser.getTenantCode());
            dto.setUserId(sessionUser.getUserId());
            dto.setAccount(sessionUser.getAccount());
            dto.setUserName(sessionUser.getUserName());
            dto.setUserType(sessionUser.getUserType());
            dto.setAuthorityPolicy(sessionUser.getAuthorityPolicy());
            dto.setLocale(sessionUser.getLocale());

            try {
                // 会话id关联token(redis或db等)
                sessionService.addSession(sessionUser.getSessionId(), sessionUser.getToken());
                result = ResultData.success(dto);
            } catch (Exception e) {
                result = ResultData.fail("登录认证异常:" + e.getMessage());
            }
            // 发布登录事件
            ApplicationContextHolder.publishEvent(new LoginEvent(loginRequest, result));
            return result;
        } else {
            result = ResultData.fail("登录认证失败:" + resultData.getMessage());
            // 发布登录事件
            ApplicationContextHolder.publishEvent(new LoginEvent(loginRequest, result));
            return result;
        }
    }
}
