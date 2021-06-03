package com.changhong.sei.auth.dto;

import com.changhong.sei.core.dto.BaseEntityDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实现功能：登录历史
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:18
 */
@ApiModel(description = "登录历史")
public class LoginHistoryDto extends BaseEntityDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 租户代码
     */
    @ApiModelProperty(notes = "租户代码")
    private String tenantCode = "none";
    /**
     * 账号
     */
    @ApiModelProperty(notes = "账号")
    private String account;
    /**
     * 登录ip
     */
    @ApiModelProperty(notes = "登录ip")
    private String loginIp;
    /**
     * 会话id
     */
    @ApiModelProperty(notes = "会话id")
    private String sessionId;
    /**
     * 登录时间
     */
    @ApiModelProperty(notes = "登录时间", example = "2020-01-14 22:18:48")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginDate;
    /**
     * 登录用户代理
     */
    @ApiModelProperty(notes = "登录客户端代理")
    private String loginUserAgent;
    /**
     * 登录日志
     */
    @ApiModelProperty(notes = "登录结果")
    private SessionUserResponse.LoginStatus loginStatus;

    @ApiModelProperty(notes = "登录日志")
    private String loginLog;
    /**
     * 浏览器
     */
    @ApiModelProperty(notes = "浏览器")
    private String browser;
    /**
     * 操作系统名
     */
    @ApiModelProperty(notes = "操作系统名")
    private String osName;

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

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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
