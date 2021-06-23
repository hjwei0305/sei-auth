package com.changhong.sei.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 实现功能：添加访问记录
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-01-14 15:54
 */
@ApiModel(description = "添加访问记录")
public class AccessRecordCreateRequest implements Serializable {

    private static final long serialVersionUID = -153710753627693002L;
    @ApiModelProperty(notes = "应用代码", required = true)
    @NotBlank
    private String appCode;
    @ApiModelProperty(notes = "功能名", required = true)
    @NotBlank
    private String feature;
    @ApiModelProperty(notes = "地址", required = true)
    @NotBlank
    private String url;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
