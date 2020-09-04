package com.changhong.sei.auth.dto;

import com.changhong.sei.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 21:04
 */
@ApiModel(description = "按账号修改账户")
public class UpdateAccountByAccountRequest implements Serializable {
    private static final long serialVersionUID = 2974541194405245535L;
    /**
     * 名称
     */
    @ApiModelProperty(notes = "租户代码", required = true)
    @NotBlank
    private String tenant;
    /**
     * 名称
     */
    @ApiModelProperty(notes = "账号", required = true)
    @NotBlank
    private String account;
    /**
     * 名称
     */
    @ApiModelProperty(notes = "名称", required = true)
    @NotBlank
    private String name;
    /**
     * 冻结
     */
    @ApiModelProperty(notes = "冻结")
    private Boolean frozen = Boolean.FALSE;
    /**
     * 锁定
     */
    @ApiModelProperty(notes = "锁定")
    private Boolean locked = Boolean.FALSE;
    /**
     * 截止有效期
     */
    @ApiModelProperty(notes = "截止有效期", example = "2020-01-21")
    @JsonFormat(timezone = DateUtils.DEFAULT_TIMEZONE, pattern = DateUtils.DEFAULT_DATE_FORMAT)
    private LocalDate accountExpired;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFrozen() {
        return frozen;
    }

    public void setFrozen(Boolean frozen) {
        this.frozen = frozen;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public LocalDate getAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(LocalDate accountExpired) {
        this.accountExpired = accountExpired;
    }
}
