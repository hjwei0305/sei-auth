package com.changhong.sei.auth.entity;

import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.entity.ITenant;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 实现功能：登录历史
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:18
 */
@Entity
@Table(name = "login_history")
@DynamicInsert
@DynamicUpdate
public class LoginHistory extends BaseEntity implements ITenant {
    private static final long serialVersionUID = 1L;
    /**
     * 租户代码
     */
    @Column(name = "tenant_code", length = 20, nullable = false)
    private String tenantCode = "none";
    /**
     * 账号
     */
    @Column(name = "account", length = 100, nullable = false)
    private String account;
    /**
     * 登录ip
     */
    @Column(name = "login_ip", length = 50)
    private String loginIp;
    /**
     * 会话id
     */
    @Column(name = "session_id", length = 36)
    private String sessionId = "none";
    /**
     * 登录时间
     */
//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "login_date", updatable = false)
    private LocalDateTime loginDate;
    /**
     * 登录用户代理
     */
    @Column(name = "login_user_agent")
    private String loginUserAgent;
    /**
     * 登录日志
     */
    @Enumerated(value = EnumType.STRING)
    @Column(name = "login_status")
    private SessionUserResponse.LoginStatus loginStatus;

    @Column(name = "login_log")
    private String loginLog;
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
    /**
     * 登出时间
     */
    @Column(name = "logout_date")
    private LocalDateTime logoutDate;

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

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public LocalDateTime getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(LocalDateTime loginDate) {
        this.loginDate = loginDate;
    }

    public String getLoginUserAgent() {
        return loginUserAgent;
    }

    public void setLoginUserAgent(String loginUserAgent) {
        this.loginUserAgent = loginUserAgent;
    }

    public SessionUserResponse.LoginStatus getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(SessionUserResponse.LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getLoginLog() {
        return loginLog;
    }

    public void setLoginLog(String loginLog) {
        this.loginLog = loginLog;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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

    public LocalDateTime getLogoutDate() {
        return logoutDate;
    }

    public void setLogoutDate(LocalDateTime logoutDate) {
        this.logoutDate = logoutDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        LoginHistory that = (LoginHistory) o;
        return Objects.equals(tenantCode, that.tenantCode) &&
                Objects.equals(account, that.account) &&
                Objects.equals(sessionId, that.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tenantCode, account, sessionId);
    }
}
