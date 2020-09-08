package com.changhong.sei.auth.api;

import com.changhong.sei.core.dto.ResultData;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;

/**
 * 实现功能：账户认证接口
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 14:13
 */
@FeignClient(name = "sei-auth", path = VerifyCodeApi.PATH)
public interface VerifyCodeApi {
    /**
     * 服务访问目录
     */
    String PATH = "verifyCode";

    /**
     * 验证码
     *
     * @param reqId 请求id
     * @return 返回验证码
     */
    @GetMapping(path = "generate")
    @ApiOperation(value = "验证码", notes = "验证码5分钟有效期")
    ResultData<String> generate(@RequestParam("reqId") @NotBlank String reqId);

    /**
     * 验证码
     *
     * @param reqId   请求id
     * @param channel 通道
     * @return 返回验证码
     */
    @GetMapping(path = "sendVerifyCode")
    @ApiOperation(value = "发送验证码", notes = "验证码5分钟有效期")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reqId", value = "请求id", required = true, paramType = "query", allowableValues = "手机号或邮箱"),
            @ApiImplicitParam(name = "channel", value = "通道", required = true, paramType = "query", allowableValues = "Mobile, EMAIL"),
            @ApiImplicitParam(name = "operation", value = "操作简介", required = true, paramType = "query")
    })
    ResultData<String> sendVerifyCode(@RequestParam("reqId") @NotBlank String reqId,
                                      @RequestParam("channel") @NotBlank String channel,
                                      @RequestParam("operation") @NotBlank String operation);

    /**
     * 验证码
     *
     * @param reqId 请求id
     * @param code  校验值
     * @return 返回验证码
     */
    @PostMapping(path = "check")
    @ApiOperation(value = "校验验证码", notes = "校验验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reqId", value = "请求id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "code", value = "校验值", required = true, paramType = "query")
    })
    ResultData<String> check(@RequestParam("reqId") @NotBlank String reqId, @RequestParam("code") @NotBlank String code);
}
