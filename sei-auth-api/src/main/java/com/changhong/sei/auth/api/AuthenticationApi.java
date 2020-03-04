package com.changhong.sei.auth.api;

import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.core.dto.ResultData;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.Set;

/**
 * 实现功能：账户认证接口
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 14:13
 */
@RequestMapping(path = "auth", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface AuthenticationApi {

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
     * 获取用户前端权限检查的功能项键值
     *
     * @param userId 用户Id
     * @return 功能项键值
     */
    @GetMapping(path = "getAuthorizedFeatures")
    @ApiOperation(value = "获取用户前端权限检查的功能项键值", notes = "获取用户前端权限检查的功能项键值(以页面路由分组)")
    ResultData<Map<String, Set<String>>> getAuthorizedFeatures(@RequestParam("userId") @NotBlank String userId);
}
