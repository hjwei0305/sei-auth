package com.changhong.sei.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 实现功能：找回密码
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-21 10:56
 */
@ApiModel(description = "找回密码")
public class FindPasswordRequest implements Serializable {
    private static final long serialVersionUID = -250452444530573741L;
    /**
     * 账户id
     */
    @ApiModelProperty(notes = "账户id", required = true)
    @NotBlank
    private String id;
    /**
     * 新账号密码
     */
    @ApiModelProperty(notes = "新账号密码.MD5散列后的值", required = true, example = "e10adc3949ba59abbe56e057f20f883e")
    @NotBlank
    private String newPassword;
    /**
     * 验证码
     */
    @ApiModelProperty(notes = "验证码", required = true)
    private String verifyCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
