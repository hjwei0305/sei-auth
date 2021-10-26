package com.changhong.sei.auth.api;

import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.OAuth2Response;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.core.dto.ResultData;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

/**
 * 实现功能：账户认证接口
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 14:13
 */
@FeignClient(name = "sei-auth")
public interface AuthenticationApi {

    /**
     * 登录
     */
    @PostMapping(path = "auth/login")
    @ApiOperation("登录")
    ResultData<SessionUserResponse> login(@RequestBody @Valid LoginRequest loginRequest);

    /**
     * 登出
     */
    @PostMapping(path = "auth/logout")
    @ApiOperation("登出")
    ResultData<String> logout(@RequestBody String sid);

    /**
     * 认证会话id
     */
    @PostMapping(path = "auth/check")
    @ApiOperation("认证会话id")
    ResultData<String> check(@RequestBody String sid);

    /**
     * 获取匿名token
     */
    @GetMapping(path = "auth/getAnonymousToken")
    @ApiOperation("获取匿名token")
    ResultData<String> getAnonymousToken();

    /**
     * 获取指定会话用户信息
     */
    @GetMapping(path = "auth/getSessionUser")
    @ApiOperation("获取指定会话用户信息")
    ResultData<SessionUserResponse> getSessionUser(@RequestParam("sid") @NotBlank String sid);

    /**
     * 通过账号签名方式认证
     *
     * @param clientId 应用标示
     * @param stamp    时间戳
     * @param account  账号
     * @param sign     签名.
     */
    @PostMapping(path = "auth/signToken/{tenantCode}")
    @ApiOperation("密码式")
    ResultData<OAuth2Response> signToken(@PathVariable(name = "tenantCode") String tenantCode,
                                         @RequestParam("clientId") String clientId,
                                         @RequestParam("account") String account,
                                         @RequestParam("timestamp") Long stamp,
                                         @RequestParam("sign") String sign,
                                         HttpServletRequest request);

    /**
     * OAuth2协议认证
     */
    @RequestMapping(path = "{tenantCode}/oauth2/{apiPath}", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("OAuth2协议认证")
    Object oauth2(@PathVariable(name = "tenantCode") String tenantCode,
                     @PathVariable(name = "apiPath") String apiPath,
                     HttpServletRequest request, HttpServletResponse response);
}
