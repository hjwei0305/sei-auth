package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.api.AuthenticationApi;
import com.changhong.sei.auth.certification.TokenAuthenticatorBuilder;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.service.SessionService;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.util.HttpUtils;
import com.changhong.sei.util.thread.ThreadLocalUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 14:21
 */
@RestController
@Api(value = "AuthenticationApi", tags = "账户认证服务")
@RequestMapping(path = AuthenticationApi.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController implements AuthenticationApi {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private TokenAuthenticatorBuilder authenticatorBuilder;

    /**
     * 登录
     * 1.账户认证
     * 2.产生会话
     * 3.会话写入缓存并设置有效期
     * 4.返回会话id
     */
    @Override
    public ResultData<SessionUserResponse> login(LoginRequest loginRequest) {
        HttpServletRequest request = HttpUtils.getRequest();
        // 客户端ip
        ThreadLocalUtil.setTranVar("ClientIP", HttpUtils.getClientIP(request));
        // 浏览器信息
        ThreadLocalUtil.setTranVar("UserAgent", HttpUtils.getUserAgent(request));

        ResultData<SessionUserResponse> resultData = authenticatorBuilder.getAuthenticator(loginRequest.getAuthType()).auth(loginRequest);
        SessionUserResponse userResponse = resultData.getData();
        if (Objects.nonNull(userResponse)) {
            // 设置当前环境
            userResponse.setEnv(ContextUtil.getProperty("spring.cloud.config.profile"));
        }
        return resultData;
    }

    /**
     * 登出
     * 清除会话id
     */
    @Override
    public ResultData<String> logout(String sid) {
        LogUtil.bizLog("登出: {}", sid);
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
     * 2.刷新id的有效期
     * 3.返回true
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
     * 获取指定会话用户信息
     */
    @Override
    public ResultData<SessionUserResponse> getSessionUser(String sid) {
        // 获取会话并续期
        String token = sessionService.getAndTouchSession(sid);
        if (StringUtils.isNotBlank(token)) {
            SessionUserResponse response = new SessionUserResponse();
            try {
                SessionUser user = ContextUtil.getSessionUser(token);
                modelMapper.map(user, response);

                return ResultData.success(response);
            } catch (Exception e) {
                LogUtil.error("获取会话信息异常", e);
                return ResultData.fail("获取会话信息异常:" + e.getMessage());
            }
        } else {
            return ResultData.fail("无会话信息");
        }
    }
}
