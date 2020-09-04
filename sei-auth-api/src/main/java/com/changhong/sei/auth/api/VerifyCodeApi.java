package com.changhong.sei.auth.api;

import com.changhong.sei.core.dto.ResultData;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping(path = "verifyCode")
    @ApiOperation(value = "验证码", notes = "验证码5分钟有效期")
    ResultData<String> verifyCode(@RequestParam("reqId") @NotBlank String reqId);
}
