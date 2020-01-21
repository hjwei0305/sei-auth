package com.changhong.sei.auth.entity;

import com.changhong.sei.auth.dto.SessionUserDto;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.entity.ITenant;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

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
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
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
    @Enumerated(value= EnumType.STRING)
    @Column(name = "login_status")
    private SessionUserDto.LoginStatus loginStatus;

    @Column(name = "login_log")
    private String loginLog;

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

    public SessionUserDto.LoginStatus getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(SessionUserDto.LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getLoginLog() {
        return loginLog;
    }

    public void setLoginLog(String loginLog) {
        this.loginLog = loginLog;
    }
}
