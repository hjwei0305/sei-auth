package com.changhong.sei.auth.entity;

import com.changhong.sei.auth.dto.ChannelEnum;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.entity.ITenant;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
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
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_OPEN_ID = "openId";
    public static final String FIELD_CHANNEL = "channel";
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
     * 主账号
     * 若account = openId,则为主账户,反之不是
     */
    @Column(name = "account", length = 100, nullable = false)
    private String account;

    /**
     * 名称
     */
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    /**
     * 社交平台开放ID
     */
    @Column(name = "open_id", length = 100)
    private String openId;
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
//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "account_expired")
    private LocalDate accountExpired;

    /**
     * 密码
     */
    @Column(name = "password_hash", length = 100)
    private String password;

    /**
     * 密码过期时间
     */
//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "password_expire_time")
    private LocalDate passwordExpireTime;

    /**
     * 来源系统(sei,wechat,dingtalk等)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "system_code", length = 50)
    private ChannelEnum channel;

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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getPasswordExpireTime() {
        return passwordExpireTime;
    }

    public void setPasswordExpireTime(LocalDate passwordExpireTime) {
        this.passwordExpireTime = passwordExpireTime;
    }

    public ChannelEnum getChannel() {
        return channel;
    }

    public void setChannel(ChannelEnum channel) {
        this.channel = channel;
    }
}
