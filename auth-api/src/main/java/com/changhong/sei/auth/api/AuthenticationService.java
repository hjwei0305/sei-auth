package com.changhong.sei.auth.api;

import com.changhong.sei.auth.dto.SessionUserDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.auth.dto.AuthDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 实现功能：账户认证接口
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 14:13
 */
@RestController
@RequestMapping(path = "auth", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface AuthenticationService {

    /**
     * 登录
     */
    @PostMapping(path = "login")
    @ApiOperation("登录")
    ResultData<SessionUserDto> login(AuthDto authDto);

    /**
     * 登出
     */
    @PostMapping(path = "logout")
    @ApiOperation("登出")
    ResultData<String> logout(String sid);

    /**
     * 认证会话id
     */
    @PostMapping(path = "check")
    @ApiOperation("认证会话id")
    ResultData<SessionUserDto> check(String sid);
}
