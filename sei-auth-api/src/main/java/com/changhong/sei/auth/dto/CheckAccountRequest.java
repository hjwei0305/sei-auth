package com.changhong.sei.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 实现功能：找回密码检查账号
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-09-07 16:02
 */
@ApiModel(description = "找回密码检查账号")
public class CheckAccountRequest implements Serializable {

    private static final long serialVersionUID = 7977208546901875698L;

    @Size(max = 36)
    @ApiModelProperty(notes = "请求id", required = true)
    private String reqId;
    /**
     * 验证码
     */
    @Size(max = 6)
    @ApiModelProperty(notes = "验证码", required = true)
    private String verifyCode;
    /**
     * 账户
     */
    @NotBlank
    @Size(max = 100)
    @ApiModelProperty(notes = "账号", required = true)
    private String openId;
    /**
     * 代码
     */
    @Size(max = 10)
    @ApiModelProperty(notes = "租户代码")
    private String tenant;

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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
