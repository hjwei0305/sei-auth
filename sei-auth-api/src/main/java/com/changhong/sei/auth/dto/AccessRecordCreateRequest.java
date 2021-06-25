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

    @NotBlank
    @ApiModelProperty(notes = "应用代码", required = true)
    private String appCode;
    @ApiModelProperty(notes = "功能代码")
    private String featureCode;
    @NotBlank
    @ApiModelProperty(notes = "功能名称", required = true)
    private String feature;
    @NotBlank
    @ApiModelProperty(notes = "地址", required = true)
    private String url;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
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
