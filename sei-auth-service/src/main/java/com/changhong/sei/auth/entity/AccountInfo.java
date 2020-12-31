package com.changhong.sei.auth.entity;

import com.changhong.sei.core.entity.BaseAuditableEntity;
import com.changhong.sei.core.entity.ITenant;
import com.changhong.sei.enums.UserAuthorityPolicy;
import com.changhong.sei.enums.UserType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 实现功能：平台账户实体
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 13:41
 */
@Entity
@Table(name = "auth_account_info")
@DynamicInsert
@DynamicUpdate
public class AccountInfo extends BaseAuditableEntity implements ITenant, Serializable {
    private static final long serialVersionUID = 1L;
    public static final String FIELD_ACCOUNT = "account";

    /**
     * 租户代码
     */
    @Column(name = "tenant_code", length = 20, nullable = false, updatable = false)
    private String tenantCode;
    /**
     * 账号
     * 主账号
     * 若account = openId,则为主账户,反之不是
     */
    @Column(name = "account", length = 100, nullable = false, updatable = false)
    private String account;
    /**
     * 移动电话
     */
    @Column(name = "mobile", length = 20)
    private String mobile;
    /**
     * 邮箱
     */
    @Column(name = "email", length = 100)
    private String email;
    /**
     * 性别 ，true表示男，false表示女
     */
    @Column(name = "gender")
    private Boolean gender = Boolean.FALSE;
    /**
     * 身份证号码
     */
    @Column(name = "id_card", length = 20)
    private String idCard;
    /**
     * 头像
     */
    @Column(name = "portrait")
    private String portrait;
    /**
     * 语言代码
     */
    @Column(name = "language_code", length = 10)
    private String languageCode = "zh_CN";

    /**
     * 账户类型(员工,客户等)
     */
    @Column(name = "account_type", length = 50)
    private String accountType = UserType.Employee.name();

    /**
     * 用户权限策略
     */
    @Column(name = "authority_policy", length = 50)
    private String authorityPolicy = UserAuthorityPolicy.NormalUser.name();

    @Override
    public String getTenantCode() {
        return tenantCode;
    }

    @Override
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
