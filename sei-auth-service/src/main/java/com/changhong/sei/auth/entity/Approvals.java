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
 * 实现功能：用户签约信息
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-10-18 10:42
 */
@Entity
@Table(name = "approvals")
@DynamicInsert
@DynamicUpdate
public class Approvals extends BaseEntity implements ITenant, Serializable {
    private static final long serialVersionUID = 676143817403479972L;
    public static final String FIELD_CLIENT_ID = "clientId";

    /**
     * 租户代码
     */
    @Column(name = "tenant_code", length = 20, nullable = false, updatable = false)
    private String tenantCode;
    /**
     * 应用标识
     */
    @Column(name = "client_id", nullable = false)
    private String clientId;
    /**
     * 用户id
     */
    @Column(name = "user_id", nullable = false)
    private String userId;
    /**
     * 应用签约的所有权限, 多个用逗号隔开
     */
    @Column(name = "contract_scope")
    private String contractScope;
    /**
     * 过期时间
     */
    @Column(name = "expiresAt")
    private LocalDateTime expiresAt;

    /**
     * 签约批准时间
     */
    @Column(name = "approvals_time")
    private LocalDateTime approvalsTime;

    @Override
    public String getTenantCode() {
        return tenantCode;
    }

    @Override
    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContractScope() {
        return contractScope;
    }

    public void setContractScope(String contractScope) {
        this.contractScope = contractScope;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getApprovalsTime() {
        return approvalsTime;
    }

    public void setApprovalsTime(LocalDateTime approvalsTime) {
        this.approvalsTime = approvalsTime;
    }
}
