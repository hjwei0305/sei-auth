package com.changhong.sei.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-21 10:56
 */
@ApiModel(description = "账户密码修改")
public class UpdatePasswordRequest implements Serializable {
    private static final long serialVersionUID = -250452444530573741L;
    /**
     * 租户代码
     */
    @ApiModelProperty(notes = "租户代码")
    @NotBlank
    private String tenant;
    /**
     * 账号
     */
    @ApiModelProperty(notes = "账号")
    @NotBlank
    private String account;
    /**
     * 新账号密码
     */
    @ApiModelProperty(notes = "新账号密码.MD5散列后的值")
    @NotBlank
    private String newPassword;
    /**
     * 旧账号密码
     */
    @ApiModelProperty(notes = "旧账号密码.MD5散列后的值")
    @NotBlank
    private String oldPassword;

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
