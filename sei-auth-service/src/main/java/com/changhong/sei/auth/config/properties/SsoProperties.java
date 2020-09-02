package com.changhong.sei.auth.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author Vision.Mac
 * @version 1.0.1 2019/3/27 20:56
 */
@ConfigurationProperties(prefix = "sei.sso")
public class SsoProperties implements Serializable {
    private static final long serialVersionUID = -1051872047414569570L;
    private boolean enable = false;
    /**
     * 集成的应用
     */
    private String app;
    /**
     * 租户代码
     */
    private String tenant;
    /**
     * 应用基地址
     */
    private String webBaseUrl;
    /**
     * 退出系统地址
     */
    private String logout;
    /**
     * 登录成功的地址
     */
    private String index;

    private String security;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getWebBaseUrl() {
        return webBaseUrl;
    }

    public void setWebBaseUrl(String webBaseUrl) {
        this.webBaseUrl = webBaseUrl;
    }

    public String getLogout() {
        return logout;
    }

    public void setLogout(String logout) {
        this.logout = logout;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }
}
