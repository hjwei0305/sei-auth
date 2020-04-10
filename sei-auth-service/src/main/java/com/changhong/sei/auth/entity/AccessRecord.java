package com.changhong.sei.auth.entity;

import com.changhong.sei.core.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 访问记录(AccessRecord)实体类
 *
 * @author sei
 * @since 2020-03-30 11:09:00
 */
@Entity
@Table(name = "access_record")
@DynamicInsert
@DynamicUpdate
public class AccessRecord extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 624986937490137178L;

    /**
     * 账号
     */
    @Column(name = "account")
    private String account;
    /**
     * 会话id
     */
    @Column(name = "session_id")
    private String sessionId;
    /**
     * 访问时间
     */
    @Column(name = "access_time")
    private String accessTime;
    /**
     * 方法
     */
    @Column(name = "method")
    private String method;
    /**
     * 访问地址
     */
    @Column(name = "url")
    private String url;
    /**
     * 参数
     */
    @Column(name = "params")
    private String params;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(String accessTime) {
        this.accessTime = accessTime;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

}