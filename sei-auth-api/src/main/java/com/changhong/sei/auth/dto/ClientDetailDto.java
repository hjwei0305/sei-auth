package com.changhong.sei.auth.dto;

import com.changhong.sei.core.dto.BaseEntityDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-10-21 17:46
 */
@ApiModel(description = "客户端信息DTO")
public class ClientDetailDto extends BaseEntityDto {
    private static final long serialVersionUID = -2204732427618978156L;
    /**
     * 租户代码
     */
    @NotBlank
    @Size(max = 10)
    @ApiModelProperty(notes = "租户代码", required = true)
    private String tenantCode;
    /**
     * 应用标识
     */
    @NotBlank
    @Size(max = 50)
    @ApiModelProperty(notes = "应用标识", required = true)
    private String appCode;
    /**
     * 客户端应用id
     */
    @NotBlank
    @Size(max = 50)
    @ApiModelProperty(notes = "客户端应用id", required = true)
    private String clientId;
    /**
     * 客户端应用名称
     */
    @NotBlank
    @Size(max = 100)
    @ApiModelProperty(notes = "客户端应用名称", required = true)
    private String clientName;
    /**
     * 客户端应用秘钥
     */
    @NotBlank
    @ApiModelProperty(notes = "客户端应用秘钥", required = true)
    private String clientSecret;
    /**
     * 应用签约的所有权限, 多个用逗号隔开
     */
    @ApiModelProperty(notes = "授权的权限")
    public String contractScope;

    /**
     * 应用允许授权的所有URL, 多个用逗号隔开
     */
    @ApiModelProperty(notes = "授权的URL")
    public String allowUrl;
    /**
     * 单点成功跳转URL
     */
    @ApiModelProperty(notes = "单点成功跳转URL")
    private String indexUrl;
    /**
     * 单点失败跳转URL
     */
    @ApiModelProperty(notes = "单点失败跳转URL")
    private String loginUrl;
    /**
     * 备注描述
     */
    @ApiModelProperty(notes = "备注描述")
    private String remark;
    /**
     * 冻结
     * 针对禁用或删除时使用
     */
    @ApiModelProperty(notes = "冻结")
    private Boolean frozen = Boolean.FALSE;
    /**
     * 有效期
     */
    @ApiModelProperty(notes = "有效期")
    private LocalDate accountExpired;

    public String getTenantCode() {
        return tenantCode;
    }

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

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
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
