package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.api.AuthenticationApi;
import com.changhong.sei.auth.common.Constants;
import com.changhong.sei.auth.common.validatecode.IVerifyCodeGen;
import com.changhong.sei.auth.common.validatecode.VerifyCode;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.service.AccountService;
import com.changhong.sei.auth.service.LoginHistoryService;
import com.changhong.sei.auth.service.SessionService;
import com.changhong.sei.core.cache.CacheBuilder;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.util.HttpUtils;
import io.swagger.annotations.Api;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 14:21
 */
@RestController
@Api(value = "AuthenticationApi", tags = "账户认证服务")
public class AuthenticationController implements AuthenticationApi {

    @Autowired
    private AccountService accountService;
    @Autowired
    private LoginHistoryService historyService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private IVerifyCodeGen iVerifyCodeGen;
    @Autowired
    private CacheBuilder cacheBuilder;

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
        ResultData<String> checkLoginData = historyService.checkLoginFailureNum(tenant, account);
        if (checkLoginData.failed()) {
            LogUtil.warn("租户[{}]账号[{}]: {}", tenant, account, checkLoginData.getMessage());
            return ResultData.success(SessionUserResponse.build().setLoginStatus(SessionUserResponse.LoginStatus.captchaError));
        }

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

        String ipAddr = "";
        try {
            // 请求ip
            ipAddr = HttpUtils.getIpAddr(HttpUtils.getRequest());
        } catch (Exception ignored) {
        }

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
                return ResultData.success(dto);
            } catch (Exception e) {
                return ResultData.fail("登录认证异常:" + e.getMessage());
            }
        } else {
            return ResultData.fail("登录认证失败:" + resultData.getMessage());
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

    /**
     * 获取用户前端权限检查的功能项键值
     *
     * @param userId 用户Id
     * @return 功能项键值
     */
    @Override
    public ResultData<Map<String, Set<String>>> getAuthorizedFeatures(String userId) {
        return accountService.getAuthorizedFeatures(userId);
    }

    /**
     * 验证码
     */
    @Override
    public ResultData<String> verifyCode(String reqId) {
        try {
            //设置长宽
            VerifyCode verifyCode = iVerifyCodeGen.generate(80, 28);
            String code = verifyCode.getCode();
            LogUtil.info("验证码: {}", code);

            // 验证码5分钟有效期
            cacheBuilder.set(Constants.VERIFY_CODE_KEY + reqId, code, (long) (5 * 60 * 1000));

            // 返回Base64编码过的字节数组字符串
            String str = Base64.encodeBase64String(verifyCode.getImgBytes());
            return ResultData.success("data:image/jpeg;base64," + str);
        } catch (IOException e) {
            LogUtil.error("验证码错误", e);
            return ResultData.fail("验证码错误");
        }
    }
}
