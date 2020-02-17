package com.changhong.sei.auth.entity;

import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.entity.ITenant;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 实现功能：平台账户实体
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 13:41
 */
@Entity
@Table(name = "auth_account")
@DynamicInsert
@DynamicUpdate
public class Account extends BaseEntity implements ITenant {
    private static final long serialVersionUID = 1L;
    public static final String FIELD_ACCOUNT = "account";
    public static final String FIELD_USER_ID = "userId";
    /**
     * 租户代码
     */
    @Column(name = "tenant_code", length = 20, nullable = false)
    private String tenantCode;

    /**
     * 用户id
     */
    @Column(name = "user_id", length = 100, nullable = false)
    private String userId;

    /**
     * 账号
     */
    @Column(name = "account", length = 100, nullable = false)
    private String account;

    /**
     * 名称
     */
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    /**
     * 来源系统
     */
    @Column(name = "system_code", length = 50)
    private String systemCode;

    /**
     * 账户类型
     */
    @Column(name = "account_type", length = 50)
    private String accountType;

    /**
     * 密码
     */
    @Column(name = "password_hash", length = 100, nullable = false)
    private String password;

    /**
     * 冻结
     * 针对禁用或删除用户时使用
     */
    @Column(name = "frozen", length = 1)
    private Boolean frozen = Boolean.FALSE;

    /**
     * 锁定
     * 针对验证码或其他业务临时暂停账户时使用
     */
    @Column(name = "locked", length = 1)
    private Boolean locked = Boolean.FALSE;

    /**
     * 注册时间
     */
    @Column(name = "since_date", updatable = false)
    private LocalDateTime sinceDate;

    /**
     * 账户有效期
     */
    @Column(name = "account_expired")
    private LocalDate accountExpired;

    @Override
    public String getTenantCode() {
        return tenantCode;
    }

    @Override
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public LocalDateTime getSinceDate() {
        return sinceDate;
    }

    public void setSinceDate(LocalDateTime sinceDate) {
        this.sinceDate = sinceDate;
    }

    public LocalDate getAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(LocalDate accountExpired) {
        this.accountExpired = accountExpired;
    }
}
