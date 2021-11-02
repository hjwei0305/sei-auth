package com.changhong.sei.auth.entity;

import com.changhong.sei.auth.dto.ChannelEnum;
import com.changhong.sei.core.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账号绑定记录(BindingRecord)实体类
 *
 * @author sei
 * @since 2020-09-04 15:24:04
 */
@Entity
@Table(name = "binding_record")
@DynamicInsert
@DynamicUpdate
public class BindingRecord extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -11858425001246419L;
    /**
     * 租户代码
     */
    @Column(name = "tenant_code")
    private String tenantCode;
    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;
    /**
     * 账号
     */
    @Column(name = "account")
    private String account;
    /**
     * 渠道代码
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "channel_code")
    private ChannelEnum channel;
    /**
     * 社交平台开放ID
     */
    @Column(name = "open_id")
    private String openId;
    /**
     * 绑定
     */
    @Column(name = "is_bind")
    private Boolean bind;
    /**
     * 操作时间
     */
    @Column(name = "operation_date")
    private LocalDateTime operationDate;

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public ChannelEnum getChannel() {
        return channel;
    }

    public void setChannel(ChannelEnum channel) {
        this.channel = channel;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Boolean getBind() {
        return bind;
    }

    public void setBind(Boolean bind) {
        this.bind = bind;
    }

    public LocalDateTime getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(LocalDateTime operationDate) {
        this.operationDate = operationDate;
    }
}