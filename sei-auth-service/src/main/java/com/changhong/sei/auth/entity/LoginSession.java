package com.changhong.sei.auth.entity;

import com.changhong.sei.core.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:18
 */
@Entity
@Table(name = "login_session")
@DynamicInsert
@DynamicUpdate
public class LoginSession extends BaseEntity {
    private static final long serialVersionUID = 1L;
    public static final String FIELD_SID = "sid";
    /**
     * 会话id
     */
    @Column(name = "sid", length = 50)
    private String sid;
    /**
     * token
     */
    @Column(name = "token", length = 500)
    private String token;
    /**
     * 登录时间
     */
    @Column(name = "login_date", updatable = false)
    private Long loginDate;
    /**
     * 过期时间
     */
    @Column(name = "expire_date")
    private Long expireDate;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Long loginDate) {
        this.loginDate = loginDate;
    }

    public Long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
    }
}
