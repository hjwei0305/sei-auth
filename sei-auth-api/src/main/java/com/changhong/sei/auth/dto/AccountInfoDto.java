package com.changhong.sei.auth.dto;

import com.changhong.sei.enums.UserAuthorityPolicy;
import com.changhong.sei.enums.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-12-30 20:46
 */
@ApiModel(description = "账户信息")
public class AccountInfoDto implements Serializable {
    private static final long serialVersionUID = -3002615955126533946L;
    /**
     * 租户代码
     */
    @ApiModelProperty(notes = "租户代码", required = true)
    @NotBlank
    protected String tenantCode;
    /**
     * 账号
     * 主账号
     * 若account = openId,则为主账户,反之不是
     */
    @ApiModelProperty(notes = "账号", required = true)
    @NotBlank
    protected String account;
    /**
     * 移动电话
     */
    @ApiModelProperty(notes = "移动电话")
    private String mobile;
    /**
     * 邮箱
     */
    @ApiModelProperty(notes = "邮箱")
    private String email;
    /**
     * 性别 ，true表示男，false表示女
     */
    @ApiModelProperty(notes = "性别")
    private Boolean gender = Boolean.FALSE;
    /**
     * 身份证号码
     */
    @ApiModelProperty(notes = "身份证号码")
    private String idCard;
    /**
     * 头像
     */
    @ApiModelProperty(notes = "头像")
    private String portrait;
    /**
     * 语言代码
     */
    @ApiModelProperty(notes = "语言代码")
    private String languageCode = "zh_CN";

    /**
     * 账户类型(员工,客户等)
     */
    @ApiModelProperty(notes = "账户类型", example = "Employee, Supplier, Customer, Expert")
    private String accountType = UserType.Employee.name();

    /**
     * 用户权限策略
     */
    @ApiModelProperty(notes = "用户权限策略", example = "NormalUser, TenantAdmin, GlobalAdmin")
    private String authorityPolicy = UserAuthorityPolicy.NormalUser.name();

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAuthorityPolicy() {
        return authorityPolicy;
    }

    public void setAuthorityPolicy(String authorityPolicy) {
        this.authorityPolicy = authorityPolicy;
    }
}
