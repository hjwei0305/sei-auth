package com.changhong.sei.auth.entity;

import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.entity.ITenant;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-01-13 10:02
 */
@Entity
@Table(name = "access_record")
@DynamicInsert
@DynamicUpdate
public class AccessRecord extends BaseEntity implements ITenant, Serializable {
    private static final long serialVersionUID = -7958165221208610549L;

    public static final String DEFAULT_VALUE = "none";
    
    /**
     * 租户代码
     */
    @Column(name = "tenant_code", length = 20, nullable = false)
    private String tenantCode = DEFAULT_VALUE;
    /**
     * 操作人
     */
    @Column(name = "user_id", length = 36, updatable = false)
    protected String userId;
    @Column(name = "user_account", length = 50, updatable = false)
    protected String userAccount;
    @Column(name = "user_name", length = 50, updatable = false)
    protected String userName;
    /**
     * 应用模块
     */
    @Column(name = "app_module")
    protected String appModule;
    /**
     * 跟踪id
     */
    @Column(name = "trace_id")
    protected String traceId;
    /**
     * 功能代码
     */
    @Column(name = "feature_code")
    private String featureCode;
    /**
     * 功能名称
     */
    @Column(name = "feature_name")
    private String feature = DEFAULT_VALUE;
    /**
     * 类型
     */
    @Column(name = "type")
    private String type;
    /**
     * 路径
     */
    @Column(name = "path")
    private String path;
    /**
     * 地址
     */
    @Column(name = "url")
    private String url;
    /**
     * 方法名
     */
    @Column(name = "method")
    private String method;
    /**
     * 状态代码
     */
    @Column(name = "status_code")
    private Integer statusCode;
    /**
     * 耗时(ms)
     */
    @Column(name = "duration")
    private Long duration;
    /**
     * ip地址
     */
    @Column(name = "ip")
    private String ip;
    /**
     * 浏览器
     */
    @Column(name = "browser")
    private String browser;
    /**
     * 操作系统名
     */
    @Column(name = "os_name")
    private String osName;
    /**
     * 访问时间
     */
    @Column(name = "access_time")
    private LocalDateTime accessTime;

    @Override
    public String getTenantCode() {
        return tenantCode;
    }

    @Override
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

    public String getAppModule() {
        return appModule;
    }

    public void setAppModule(String appModule) {
        this.appModule = appModule;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
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

    public LocalDateTime getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(LocalDateTime accessTime) {
        this.accessTime = accessTime;
    }
}
