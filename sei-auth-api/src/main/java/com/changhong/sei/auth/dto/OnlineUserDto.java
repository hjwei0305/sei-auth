package com.changhong.sei.auth.dto;

import com.changhong.sei.core.dto.BaseEntityDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:18
 */
@ApiModel(description = "在线用户")
public class OnlineUserDto extends BaseEntityDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 会话id
     */
    @ApiModelProperty(notes = "会话id")
    private String sid;
    /**
     * 用户id
     */
    @ApiModelProperty(notes = "用户id")
    private String userId;
    /**
     * 用户账号
     */
    @ApiModelProperty(notes = "用户账号")
    private String userAccount;
    /**
     * 用户名称
     */
    @ApiModelProperty(notes = "用户名称")
    private String userName;
    /**
     * 登录时间
     */
    @ApiModelProperty(notes = "登录时间", example = "2020-01-14 22:18:48")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginDate;
    /**
     * 登录ip
     */
    @ApiModelProperty(notes = "登录ip")
    private String loginIp;
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
//    /**
//     * 过期时间
//     */
//     @ApiModelProperty(notes = "过期时间")
//    private Long expireDate;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
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
