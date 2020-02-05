package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.api.AuthenticationApi;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.service.AccountService;
import com.changhong.sei.auth.service.SessionService;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.HttpUtils;
import io.swagger.annotations.Api;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 14:21
 */
@Service
@Api(value = "AuthenticationApi", tags = "账户认证服务")
public class AuthenticationController implements AuthenticationApi {

    @Autowired
    private AccountService accountService;
    @Autowired
    private SessionService sessionService;

    /**
     * 登录
     * 1.账户认证
     * ​2.产生会话
     * 3.会话写入缓存并设置有效期
     * 4.返回会话id​​
     */
    @Override
    public ResultData<SessionUserResponse> login(LoginRequest loginRequest) {
        String tenant = loginRequest.getTenant();
        String account = loginRequest.getAccount();
        String password = loginRequest.getPassword();
        // 认证码检查,登录错误指定次数后要求输入验证码
//        if (租户+账号 检查验证码) {
//            //return ResultData.fail("认证码错误!");
//            return ResultData.success(SessionUserResponse.build().setLoginStatus(SessionUserResponse.LoginStatus.captchaError));
//        }

        Account entity;
        if (StringUtils.isBlank(tenant)) {
            List<Account> accounts = accountService.getByAccount(account);
            if (CollectionUtils.isEmpty(accounts)) {
                return ResultData.fail("账号密码错误,认证失败!");
            }

            if (accounts.size() > 1) {
                return ResultData.success("请指定租户代码", SessionUserResponse.build().setLoginStatus(SessionUserResponse.LoginStatus.multiTenant));
            }
            entity = accounts.get(0);
        } else {
            entity = accountService.getByAccountAndTenantCode(account, tenant);
        }

        if (Objects.isNull(entity)) {
            return ResultData.fail("账号密码错误,认证失败!");
        }

        // 验证密码
        if (!accountService.verifyPassword(password, entity.getPassword())) {
            return ResultData.fail("账号密码错误,认证失败!");
        }
        // 检查是否被锁定
        if (accountService.checkLocked(entity)) {
            return ResultData.success("账号被锁定,认证失败!", SessionUserResponse.build().setLoginStatus(SessionUserResponse.LoginStatus.locked));
        }
        // 检查是否被冻结
        if (accountService.checkFrozen(entity)) {
            return ResultData.success("账号被冻结,认证失败!", SessionUserResponse.build().setLoginStatus(SessionUserResponse.LoginStatus.frozen));
        }
        // 检查账户是否在有效期内
        if (!accountService.checkAccountExpired(entity)) {
            return ResultData.success("账号已过期,认证失败!", SessionUserResponse.build().setLoginStatus(SessionUserResponse.LoginStatus.expire));
        }

        SessionUser sessionUser = new SessionUser();
        sessionUser.setTenantCode(entity.getTenantCode());
        sessionUser.setUserId(entity.getUserId());
        sessionUser.setAccount(entity.getAccount());
        sessionUser.setUserName(entity.getName());
        try {
            // 请求ip
            sessionUser.setIp(HttpUtils.getIpAddr(HttpUtils.getRequest()));
        } catch (Exception ignored) {
        }
        // 设置语言
        sessionUser.setLocale(loginRequest.getLocale());
        // 生产token
        ContextUtil.generateToken(sessionUser);

        SessionUserResponse dto = SessionUserResponse.build().setLoginStatus(SessionUserResponse.LoginStatus.success);
        dto.setSessionId(sessionUser.getSessionId());
        dto.setTenantCode(sessionUser.getTenantCode());
        dto.setUserId(sessionUser.getUserId());
        dto.setAccount(sessionUser.getAccount());
        dto.setUserName(sessionUser.getUserName());
        dto.setLocale(sessionUser.getLocale());

        try {
            // 会话id关联token(redis或db等)
            sessionService.addSession(sessionUser.getSessionId(), sessionUser.getToken());
            return ResultData.success(dto);
        } catch (Exception e) {
            return ResultData.fail("登录认证异常:" + e.getMessage());
        }
    }

    /**
     * 登出
     * 清除会话id
     */
    @Override
    public ResultData<String> logout(String sid) {
        try {
            sessionService.removeSession(sid, 0);
            return ResultData.success("OK");
        } catch (Exception e) {
            return ResultData.fail("登出异常:" + e.getMessage());
        }
    }

    /**
     * 认证会话id
     * 1.通过id获取会话内容(未获取到直接返回false)
     * ​2.刷新id的有效期
     * 3.返回true​
     */
    @Override
    public ResultData<String> check(String sid) {
        try {
            // 获取会话并续期
            String token = sessionService.getAndTouchSession(sid);
            if (StringUtils.isNotBlank(token)) {
                return ResultData.success(token);
            } else {
                return ResultData.fail("认证失败");
            }
        } catch (Exception e) {
            return ResultData.fail("认证会话id异常:" + e.getMessage());
        }
    }

    /**
     * 获取匿名token
     */
    @Override
    public ResultData<String> getAnonymousToken() {
        SessionUser sessionUser = new SessionUser();
        sessionUser.setTenantCode("anonymous");
        sessionUser.setUserId("anonymous");
        sessionUser.setAccount("anonymous");
        sessionUser.setUserName("anonymous");
//        sessionUser.setEmail("anonymous");
        ContextUtil.generateToken(sessionUser);
        return ResultData.success(sessionUser.getToken());
    }
}
