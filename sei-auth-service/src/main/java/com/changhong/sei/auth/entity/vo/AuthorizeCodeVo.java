package com.changhong.sei.auth.entity.vo;

import java.io.Serializable;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-10-21 23:22
 */
public class AuthorizeCodeVo implements Serializable {
    private static final long serialVersionUID = -9201455997991759801L;
    /**
     * 授权码
     */
    private String tenantCode;
    /**
     * 授权码
     */
    private String code;

    /**
     * 应用id
     */
    private String clientId;
    /**
     * 当前登录会话id
     */
    private String sid;
    /**
     * 授权范围
     */
    private String scope;

    /**
     * 重定向的地址
     */
    private String redirectUri;

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
