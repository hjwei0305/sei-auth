package com.changhong.sei.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 21:04
 */
@ApiModel(description = "注册账户")
public class RegisterAccountRequest extends AccountResponse {
    private static final long serialVersionUID = 2974541194405245535L;
    /**
     * 密码
     */
    @ApiModelProperty(notes = "密码(md5散列后的值)", required = true, example = "e10adc3949ba59abbe56e057f20f883e")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
