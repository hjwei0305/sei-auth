package com.changhong.sei.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.StringJoiner;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 14:15
 */
@ApiModel(description = "账户认证登录")
public class LoginRequest implements Serializable {
    private static final long serialVersionUID = -2149001770273260656L;
    /**
     * 请求id
     * 社交账号绑定时是openId
     */
    @NotBlank
    @Size(max = 36)
    @ApiModelProperty(notes = "请求id.社交账号绑定时是openId的值", required = true)
    private String reqId;
    /**
     * 验证码
     */
    @Size(max = 6)
    @ApiModelProperty(notes = "验证码")
    private String verifyCode;
    /**
     * 代码
     */
    @Size(max = 10)
    @ApiModelProperty(notes = "租户代码")
    private String tenant;
    /**
     * 账户
     */
    @NotBlank
    @Size(max = 100)
    @ApiModelProperty(notes = "账户", required = true)
    private String account;
    /**
     * 密码
     */
    @NotBlank
    @Size(max = 100)
    @ApiModelProperty(notes = "密码(md5散列后的值)", required = true, example = "e10adc3949ba59abbe56e057f20f883e")
    private String password;
    /**
     * 语言环境
     */
    @Size(max = 5)
    @ApiModelProperty(notes = "语言环境")
    private String locale = "zh_CN";

    /**
     * weChat: 微信
     * password: 账号密码
     * refresh_token: 刷新token
     * captcha: 验证码+账号密码
     */
    @ApiModelProperty(value = "认证类型", example = "captcha", allowableValues = "captcha,password,weChat")
    private String authType = "captcha";

//    /**
//     * 前端界面点击清空缓存时调用
//     */
//    @ApiModelProperty(value = "刷新token")
//    private String refreshToken;

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LoginRequest.class.getSimpleName() + "[", "]")
                .add("tenant='" + tenant + "'")
                .add("account='" + account + "'")
                .add("locale='" + locale + "'")
                .toString();
    }
}
