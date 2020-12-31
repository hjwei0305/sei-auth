package com.changhong.sei.auth.api;

import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.core.dto.ResultData;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 实现功能：账户认证接口
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 14:13
 */
@FeignClient(name = "sei-auth", path = AuthenticationApi.PATH)
public interface AuthenticationApi {
    /**
     * 服务访问目录
     */
    String PATH = "auth";

    /**
     * 登录
     */
    @PostMapping(path = "login")
    @ApiOperation("登录")
    ResultData<SessionUserResponse> login(@RequestBody @Valid LoginRequest loginRequest);

    /**
     * 登出
     */
    @PostMapping(path = "logout")
    @ApiOperation("登出")
    ResultData<String> logout(@RequestBody String sid);

    /**
     * 认证会话id
     */
    @PostMapping(path = "check")
    @ApiOperation("认证会话id")
    ResultData<String> check(@RequestBody String sid);

    /**
     * 获取匿名token
     */
    @GetMapping(path = "getAnonymousToken")
    @ApiOperation("获取匿名token")
    ResultData<String> getAnonymousToken();

    /**
     * 获取指定会话用户信息
     */
    @GetMapping(path = "getSessionUser")
    @ApiOperation("获取指定会话用户信息")
    ResultData<SessionUserResponse> getSessionUser(@RequestParam("sid") @NotBlank String sid);
}
