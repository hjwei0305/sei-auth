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
@ApiModel(description = "修改账户头像")
public class UpdateAvatarRequest implements Serializable {
    private static final long serialVersionUID = -250452444530573741L;
    /**
     * 租户代码
     */
    @ApiModelProperty(notes = "租户代码", required = true)
    @NotBlank
    private String tenant;
    /**
     * 账号
     */
    @ApiModelProperty(notes = "账号", required = true)
    @NotBlank
    private String account;
    /**
     * 新账号密码
     */
    @ApiModelProperty(notes = "头像图片(base64)", required = true)
    @NotBlank
    private String avatar;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
