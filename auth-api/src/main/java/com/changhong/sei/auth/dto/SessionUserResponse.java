package com.changhong.sei.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 22:05
 */
@ApiModel(description = "SessionUserResponse")
public class SessionUserResponse implements Serializable {
    private static final long serialVersionUID = 6402761734842712786L;
    /**
     * 会话id
     */
    @ApiModelProperty(notes = "会话id")
    private String sessionId;
    /**
     * 用户id，平台唯一
     */
    @ApiModelProperty(notes = "用户id")
    private String userId;
    /**
     * 用户账号
     */
    @ApiModelProperty(notes = "用户账号")
    private String account;
    /**
     * 用户名
     */
    @ApiModelProperty(notes = "用户名")
    private String userName;
    /**
     * 租户代码
     */
    @ApiModelProperty(notes = "租户代码")
    private String tenantCode;
    /**
     * 语言环境
     */
    @ApiModelProperty(notes = "语言环境")
    private String locale = "zh_CN";
    /**
     * 客户端IP
     */
    @ApiModelProperty(notes = "登录状态")
    private LoginStatus loginStatus;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public LoginStatus getLoginStatus() {
        return loginStatus;
    }

    public SessionUserResponse setLoginStatus(LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
        return this;
    }

    public enum LoginStatus {
        /**
         * 登录成功
         */
        success,

        /**
         * 登录失败
         * 账号密码错误或账号不存在
         */
        failure,

        /**
         * 多租户
         * 登录时需要传入租户代码
         */
        multiTenant,

        /**
         * 验证码错误
         */
        captchaError,

        /**
         * 账号被冻结
         */
        frozen,

        /**
         * 账号被锁定
         */
        locked,

        /**
         * 账号过期
         */
        expire
    }

    public static SessionUserResponse build() {
        return new SessionUserResponse();
    }

}
