package com.changhong.sei.auth.entity;

import com.changhong.sei.core.entity.BaseEntity;
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
public class AccessRecord extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -7958165221208610549L;
    /**
     * 租户代码
     */
    @Column(name = "tenant_code", length = 20, nullable = false)
    private String tenantCode = "none";
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
     * 操作
     */
    @Column(name = "feature")
    private String feature;
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

    public String getTenantCode() {
        return tenantCode;
    }

    public AccessRecord setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public AccessRecord setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public AccessRecord setUserAccount(String userAccount) {
        this.userAccount = userAccount;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public AccessRecord setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getAppModule() {
        return appModule;
    }

    public AccessRecord setAppModule(String appModule) {
        this.appModule = appModule;
        return this;
    }

    public String getTraceId() {
        return traceId;
    }

    public AccessRecord setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public String getFeature() {
        return feature;
    }

    public AccessRecord setFeature(String feature) {
        this.feature = feature;
        return this;
    }

    public String getType() {
        return type;
    }

    public AccessRecord setType(String type) {
        this.type = type;
        return this;
    }

    public String getPath() {
        return path;
    }

    public AccessRecord setPath(String path) {
        this.path = path;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public AccessRecord setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public AccessRecord setMethod(String method) {
        this.method = method;
        return this;
    }

    public Long getDuration() {
        return duration;
    }

    public AccessRecord setDuration(Long duration) {
        this.duration = duration;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public AccessRecord setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getBrowser() {
        return browser;
    }

    public AccessRecord setBrowser(String browser) {
        this.browser = browser;
        return this;
    }

    public String getOsName() {
        return osName;
    }

    public AccessRecord setOsName(String osName) {
        this.osName = osName;
        return this;
    }

    public LocalDateTime getAccessTime() {
        return accessTime;
    }

    public AccessRecord setAccessTime(LocalDateTime accessTime) {
        this.accessTime = accessTime;
        return this;
    }
}
