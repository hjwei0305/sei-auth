package com.changhong.sei.auth.dto;

import com.changhong.sei.core.dto.BaseEntityDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 访问记录(AccessRecord)DTO类
 *
 * @author sei
 * @since 2021-01-14 15:57:56
 */
@ApiModel(description = "访问记录DTO")
public class AccessRecordFeatureResponse extends BaseEntityDto {
    private static final long serialVersionUID = 932573143237651356L;
    /**
     * 应用模块
     */
    @ApiModelProperty(value = "应用模块")
    private String appModule;
    /**
     * 功能
     */
    @ApiModelProperty(value = "功能")
    private String feature;

    /**
     * 访问路径
     */
    @ApiModelProperty(value = "访问路径")
    private String path;

    @ApiModelProperty(value = "数量")
    private long countNum;

    @ApiModelProperty(value = "最近访问时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime accessTime;

    public AccessRecordFeatureResponse() {
    }

    public AccessRecordFeatureResponse(String appModule, String feature, String path, long countNum, LocalDateTime accessTime) {
        this.appModule = appModule;
        this.feature = feature;
        this.path = path;
        this.countNum = countNum;
        this.accessTime = accessTime;
    }

    public String getAppModule() {
        return appModule;
    }

    public void setAppModule(String appModule) {
        this.appModule = appModule;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getCountNum() {
        return countNum;
    }

    public void setCountNum(long countNum) {
        this.countNum = countNum;
    }

    public LocalDateTime getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(LocalDateTime accessTime) {
        this.accessTime = accessTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccessRecordFeatureResponse that = (AccessRecordFeatureResponse) o;

        if (!Objects.equals(appModule, that.appModule)) {
            return false;
        }
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        int result = appModule != null ? appModule.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }
}