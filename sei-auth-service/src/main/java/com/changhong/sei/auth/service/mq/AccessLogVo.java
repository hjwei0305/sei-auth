package com.changhong.sei.auth.service.mq;

import java.io.Serializable;

/**
 * 实现功能：访问日志vo
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-01-13 10:02
 */
public class AccessLogVo implements Serializable {
    private static final long serialVersionUID = -7958165221208610549L;

    protected String token;
    /**
     * 应用模块
     */
    protected String appModule;
    /**
     * 跟踪id
     */
    protected String traceId;
    /**
     * 路径
     */
    private String path;
    /**
     * 地址
     */
    private String url;
    /**
     * 方法名
     */
    private String method;
    /**
     * 耗时(ms)
     */
    private Long duration;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 终端用户代理
     */
    private String userAgent;
    /**
     * 操作时间(Unix时间戳)
     */
    private Long accessTime;

    public AccessLogVo() {

    }

    public AccessLogVo(String traceId, String token, String path, String method) {
        this.traceId = traceId;
        this.token = token;
        this.path = path;
        this.method = method;
    }

    public String getToken() {
        return token;
    }

    public AccessLogVo setToken(String token) {
        this.token = token;
        return this;
    }

    public String getAppModule() {
        return appModule;
    }

    public AccessLogVo setAppModule(String appModule) {
        this.appModule = appModule;
        return this;
    }

    public String getTraceId() {
        return traceId;
    }

    public AccessLogVo setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public String getPath() {
        return path;
    }

    public AccessLogVo setPath(String path) {
        this.path = path;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public AccessLogVo setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public AccessLogVo setMethod(String method) {
        this.method = method;
        return this;
    }

    public Long getDuration() {
        return duration;
    }

    public AccessLogVo setDuration(Long duration) {
        this.duration = duration;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public AccessLogVo setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public AccessLogVo setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public Long getAccessTime() {
        return accessTime;
    }

    public AccessLogVo setAccessTime(Long accessTime) {
        this.accessTime = accessTime;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccessLogVo that = (AccessLogVo) o;

        return traceId.equals(that.traceId);
    }

    @Override
    public int hashCode() {
        return traceId.hashCode();
    }
}
