package com.changhong.sei.auth.dto;

import com.chonghong.sei.enums.UserAuthorityPolicy;
import com.chonghong.sei.enums.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 22:05
 */
@ApiModel(description = "SessionUserDto")
public class SessionUserDto implements Serializable {
    private static final long serialVersionUID = 6402761734842712786L;
    /**
     * 会话id
     */
    @ApiModelProperty(notes = "会话id")
    private String sessionId;
    /**
     * token
     */
    @ApiModelProperty(notes = "token")
    private String token;
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
     * 邮箱
     */
    @ApiModelProperty(notes = "邮箱")
    private String email;
    /**
     * 用户类型
     */
    @ApiModelProperty(notes = "用户类型")
    private UserType userType = UserType.Employee;
    /**
     * 用户权限策略
     */
    @ApiModelProperty(notes = "用户权限策略")
    private UserAuthorityPolicy authorityPolicy = UserAuthorityPolicy.NormalUser;
    /**
     * 客户端IP
     */
    @ApiModelProperty(notes = "客户端IP")
    private String ip;
    /**
     * 语言环境
     */
    @ApiModelProperty(notes = "语言环境")
    private String locale = "zh_CN";

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
