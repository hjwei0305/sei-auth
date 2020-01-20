package com.changhong.sei.auth.dto;

import com.changhong.sei.core.dto.BaseEntityDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 21:04
 */
@ApiModel(description = "账户DTO")
public class AccountDto extends BaseEntityDto {
    private static final long serialVersionUID = 2974541194405245535L;
    /**
     * 租户代码
     */
    @ApiModelProperty(notes = "租户代码")
    private String tenantCode;
    /**
     * 用户id
     */
    @ApiModelProperty(notes = "用户id")
    private String userId;
    /**
     * 账号
     */
    @ApiModelProperty(notes = "账号")
    private String account;
    /**
     * 名称
     */
    @ApiModelProperty(notes = "名称")
    private String name;
    /**
     * 来源系统
     */
    @ApiModelProperty(notes = "来源系统")
    private String systemCode;
    /**
     * 账户类型
     */
    @ApiModelProperty(notes = "账户类型")
    private String accountType;
    /**
     * 密码
     */
    @ApiModelProperty(notes = "密码")
    private String passwordHash;
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
    private Date sinceDate;
    /**
     * 开始有效期
     */
    @ApiModelProperty(notes = "开始有效期")
    private Date startValidity;
    /**
     * 截止有效期
     */
    @ApiModelProperty(notes = "截止有效期")
    private Date endValidity;

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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

    public Date getStartValidity() {
        return startValidity;
    }

    public void setStartValidity(Date startValidity) {
        this.startValidity = startValidity;
    }

    public Date getEndValidity() {
        return endValidity;
    }

    public void setEndValidity(Date endValidity) {
        this.endValidity = endValidity;
    }
}
