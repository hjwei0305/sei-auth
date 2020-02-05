package com.changhong.sei.auth.dto;

import com.changhong.sei.core.dto.BaseEntityDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 21:04
 */
@ApiModel(description = "账户信息")
public class AccountResponse extends BaseEntityDto {
    private static final long serialVersionUID = 2974541194405245535L;
    /**
     * 租户代码
     */
    @ApiModelProperty(notes = "租户代码")
    @NotBlank
    private String tenantCode;
    /**
     * 用户id
     */
    @ApiModelProperty(notes = "用户id")
    @NotBlank
    private String userId;
    /**
     * 账号
     */
    @ApiModelProperty(notes = "账号")
    @NotBlank
    private String account;
    /**
     * 名称
     */
    @ApiModelProperty(notes = "名称")
    @NotBlank
    private String name;
    /**
     * 来源系统
     */
    @ApiModelProperty(notes = "来源系统")
    @NotBlank
    private String systemCode;
    /**
     * 账户类型
     */
    @ApiModelProperty(notes = "账户类型")
    @NotBlank
    private String accountType;
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
     * 注册时间
     */
    @ApiModelProperty(notes = "注册时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sinceDate;
    /**
     * 截止有效期
     */
    @ApiModelProperty(notes = "截止有效期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date validityDate;

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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

    public Date getSinceDate() {
        return sinceDate;
    }

    public void setSinceDate(Date sinceDate) {
        this.sinceDate = sinceDate;
    }

    public Date getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(Date validityDate) {
        this.validityDate = validityDate;
    }
}