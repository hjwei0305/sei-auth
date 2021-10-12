package com.changhong.sei.auth.entity;

import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.entity.ITenant;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:18
 */
@Entity
@Table(name = "online_user")
@DynamicInsert
@DynamicUpdate
public class OnlineUser extends BaseEntity implements ITenant {
    private static final long serialVersionUID = 1L;
    /**
     * 会话id
     */
    @Column(name = "session_id", length = 50)
    private String sid;
    /**
     * 租户代码
     */
    @Column(name = "tenant_code", length = 20, nullable = false)
    private String tenantCode = "none";
    /**
     * 用户id
     */
    @Column(name = "user_id", length = 36, nullable = false)
    private String userId;
    /**
     * 用户账号
     */
    @Column(name = "user_account", length = 100, nullable = false)
    private String userAccount;
    /**
     * 用户名称
     */
    @Column(name = "user_name", length = 100, nullable = false)
    private String userName;
    /**
     * 登录时间
     */
    @Column(name = "login_date", updatable = false)
    private LocalDateTime loginDate;
    /**
     * 登录时间戳
     */
    @Column(name = "login_timestamp", updatable = false)
    private Long timestamp = 0L;
    /**
     * 登录用户代理
     */
    @Column(name = "login_user_agent")
    private String loginUserAgent;
    /**
     * 登录ip
     */
    @Column(name = "login_ip", length = 50)
    private String loginIp;
    /**
     * 浏览器
     */
    @Column(name = "browser")
    private String browser;
    /**
     * 操作系统名
     */
    @Column(name = "os_name")
    private String osName;
//    /**
//     * 过期时间
//     */
//    @Transient
//    private Long expireDate;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

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

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(LocalDateTime loginDate) {
        this.loginDate = loginDate;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLoginUserAgent() {
        return loginUserAgent;
    }

    public void setLoginUserAgent(String loginUserAgent) {
        this.loginUserAgent = loginUserAgent;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }
}
