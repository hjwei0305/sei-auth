package com.changhong.sei.auth.entity;

import com.changhong.sei.core.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

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
public class Account extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 租户代码
     */
    @Column(name = "tenant", length = 20, nullable = false)
    private String tenant;

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
    @Column(name = "password", length = 100, nullable = false)
    private String password;

    /**
     * 冻结
     */
    @Column(name = "frozen", length = 1)
    private Boolean frozen = Boolean.FALSE;

    /**
     * 锁定
     */
    @Column(name = "locked", length = 1)
    private Boolean locked = Boolean.FALSE;

    /**
     * 注册时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "since_date", updatable = false)
    private Date sinceDate;

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

    public Date getSinceDate() {
        return sinceDate;
    }

    public void setSinceDate(Date sinceDate) {
        this.sinceDate = sinceDate;
    }
}
