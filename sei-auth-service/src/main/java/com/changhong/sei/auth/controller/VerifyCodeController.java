package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.api.AuthenticationApi;
import com.changhong.sei.auth.api.VerifyCodeApi;
import com.changhong.sei.auth.service.ValidateCodeService;
import com.changhong.sei.core.dto.ResultData;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-09-04 13:25
 */
@RestController
@Api(value = "VerifyCodeApi", tags = "验证码服务")
@RequestMapping(path = VerifyCodeApi.PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class VerifyCodeController implements VerifyCodeApi {

    @Autowired
    private ValidateCodeService validateCodeService;

    /**
     * 验证码
     *
     * @param reqId 请求id
     * @return 返回验证码
     */
    @Override
    public ResultData<String> verifyCode(@NotBlank String reqId) {
        return validateCodeService.generate(reqId);
    }
}
