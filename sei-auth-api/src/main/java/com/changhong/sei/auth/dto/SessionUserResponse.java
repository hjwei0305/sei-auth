package com.changhong.sei.auth.dto;

import com.changhong.sei.enums.UserAuthorityPolicy;
import com.changhong.sei.enums.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.StringJoiner;

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
     * 社交平台开放ID
     */
    @ApiModelProperty(notes = "社交平台开放ID")
    private String openId;
    /**
     * 用户id，平台唯一
     */
    @ApiModelProperty(notes = "用户id")
    private String userId;
    /**
     * 用户主账号
     */
    @ApiModelProperty(notes = "用户主账号")
    private String account;
    /**
     * 当前登录账号
     */
    @ApiModelProperty(notes = "当前登录账号")
    private String loginAccount;
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
     * 用户类型
     */
    @ApiModelProperty(notes = "用户类型(enum)")
    private UserType userType = UserType.Employee;
    /**
     * 用户权限策略
     */
    @ApiModelProperty(notes = "用户权限策略(enum)")
    private UserAuthorityPolicy authorityPolicy = UserAuthorityPolicy.NormalUser;
    /**
     * 语言环境
     */
    @ApiModelProperty(notes = "语言环境")
    private String locale = "zh_CN";
    /**
     * 登录状态
     */
    @ApiModelProperty(notes = "登录状态(enum)")
    private LoginStatus loginStatus;
    /**
     * 当前环境
     */
    @ApiModelProperty(notes = "当前环境")
    private String env;

    /**
     * 跳转地址
     */
    @ApiModelProperty(notes = "跳转地址")
    private String redirectUrl;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
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

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
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

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserAuthorityPolicy getAuthorityPolicy() {
        return authorityPolicy;
    }

    public void setAuthorityPolicy(UserAuthorityPolicy authorityPolicy) {
        this.authorityPolicy = authorityPolicy;
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

    public String getEnv() {
        return env;
    }

    public SessionUserResponse setEnv(String env) {
        this.env = env;
        return this;
    }

    public enum LoginStatus {
        /**
         * 登录成功
         */
        success,

        /**
         * 登录失败
         * 账号或密码错误
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
        expire,

        /**
         * 密码过期
         */
        passwordExpire
    }

    public static SessionUserResponse build() {
        return new SessionUserResponse();
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SessionUserResponse.class.getSimpleName() + "[", "]")
                .add("sessionId='" + sessionId + "'")
                .add("openId='" + openId + "'")
                .add("userId='" + userId + "'")
                .add("account='" + account + "'")
                .add("loginAccount='" + loginAccount + "'")
                .add("userName='" + userName + "'")
                .add("tenantCode='" + tenantCode + "'")
                .add("loginStatus=" + loginStatus)
                .add("env=" + env)
                .add("redirectUrl='" + redirectUrl + "'")
                .toString();
    }
}
