package com.changhong.sei.auth.dto;

import com.changhong.sei.core.dto.BaseEntityDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 访问记录(AccessRecord)DTO类
 *
 * @author sei
 * @since 2021-01-14 15:57:56
 */
@ApiModel(description = "访问记录DTO")
public class AccessRecordDto extends BaseEntityDto {
    private static final long serialVersionUID = 932573143237651356L;
    /**
     * 租户代码
     */
    @ApiModelProperty(value = "租户代码")
    private String tenantCode;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;
    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    private String userAccount;
    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    private String userName;
    /**
     * 跟踪id
     */
    @ApiModelProperty(value = "跟踪id")
    private String traceId;
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
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private String type;
    /**
     * 访问路径
     */
    @ApiModelProperty(value = "访问路径")
    private String path;
    /**
     * 方法名
     */
    @ApiModelProperty(value = "方法名")
    private String method;
    /**
     * 耗时(ms)
     */
    @ApiModelProperty(value = "耗时(ms)")
    private Integer duration;
    /**
     * 客户端ip
     */
    @ApiModelProperty(value = "客户端ip")
    private String ip;
    /**
     * 客户端浏览器
     */
    @ApiModelProperty(value = "客户端浏览器")
    private String browser;
    /**
     * 客户端os
     */
    @ApiModelProperty(value = "客户端os")
    private String osName;
    /**
     * 访问时间
     */
    @ApiModelProperty(value = "访问时间")
    private Date accessTime;

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
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

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

}