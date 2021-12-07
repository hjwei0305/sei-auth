package com.changhong.sei.auth.entity;

import com.changhong.sei.core.entity.BaseAuditableEntity;
import com.changhong.sei.core.entity.ITenant;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-10-18 10:42
 */
@Entity
@Table(name = "client_detail")
@DynamicInsert
@DynamicUpdate
public class ClientDetail extends BaseAuditableEntity implements ITenant, Serializable {
    private static final long serialVersionUID = 676143817403479972L;
    public static final String FIELD_CLIENT_ID = "clientId";
    public static final String FIELD_APP_CODE = "appCode";

    /**
     * 租户代码
     */
    @Column(name = "tenant_code", length = 20, nullable = false, updatable = false)
    private String tenantCode;
    /**
     * 应用标识
     */
    @Column(name = "app_code", nullable = false)
    private String appCode;
    /**
     * 客户端应用id
     */
    @Column(name = "client_id", nullable = false)
    private String clientId;
    /**
     * 客户端应用名称
     */
    @Column(name = "client_name", nullable = false)
    private String clientName;
    /**
     * 客户端应用秘钥
     */
    @Column(name = "client_secret", nullable = false)
    private String clientSecret;
    /**
     * 应用签约的所有权限, 多个用逗号隔开
     */
    @Column(name = "contract_scope")
    private String contractScope;

    /**
     * 应用允许授权的所有URL, 多个用逗号隔开
     */
    @Column(name = "allow_url")
    private String allowUrl;
    /**
     * 备注描述
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 冻结
     * 针对禁用或删除时使用
     */
    @Column(name = "frozen", length = 1)
    private Boolean frozen = Boolean.FALSE;
    /**
     * 有效期
     */
    @Column(name = "account_expired")
    private LocalDate accountExpired;

    @Override
    public String getTenantCode() {
        return tenantCode;
    }

    @Override
    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getContractScope() {
        return contractScope;
    }

    public void setContractScope(String contractScope) {
        this.contractScope = contractScope;
    }

    public String getAllowUrl() {
        return allowUrl;
    }

    public void setAllowUrl(String allowUrl) {
        this.allowUrl = allowUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getFrozen() {
        return frozen;
    }

    public void setFrozen(Boolean frozen) {
        this.frozen = frozen;
    }

    public LocalDate getAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(LocalDate accountExpired) {
        this.accountExpired = accountExpired;
    }
}
