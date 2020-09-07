package com.changhong.sei.auth.dto;

import com.changhong.sei.core.dto.annotation.Desensitization;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 实现功能：找回密码检查账号
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-09-07 16:02
 */
@ApiModel(description = "找回密码检查账号")
public class CheckAccountResponse implements Serializable {
    private static final long serialVersionUID = 7977208546901875698L;

    /**
     * 账户
     */
    @ApiModelProperty(notes = "id")
    private String id;
    /**
     * 账户
     */
    @ApiModelProperty(notes = "账号")
    private String openId;
    /**
     * 代码
     */
    @Desensitization(Desensitization.DesensitizationType.EMAIL)
    @ApiModelProperty(notes = "邮箱")
    private String email;

    @Desensitization(Desensitization.DesensitizationType.MOBILE_PHONE)
    @ApiModelProperty(notes = "手机号")
    private String mobile;

    @ApiModelProperty(notes = "检查结果")
    private String result;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
