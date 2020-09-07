package com.changhong.sei.auth.entity;

import com.changhong.sei.auth.dto.ChannelEnum;
import com.changhong.sei.core.entity.BaseAuditableEntity;
import com.changhong.sei.core.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_OPEN_ID = "openId";
    public static final String FIELD_CHANNEL = "channel";
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
     * 注册时间
     */
    @Column(name = "binding_date")
    private LocalDateTime bindingDate;
    /**
     * 注册时间
     */
    @Column(name = "unbinding_date")
    private LocalDateTime unbindingDate;


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

    public LocalDateTime getBindingDate() {
        return bindingDate;
    }

    public void setBindingDate(LocalDateTime bindingDate) {
        this.bindingDate = bindingDate;
    }

    public LocalDateTime getUnbindingDate() {
        return unbindingDate;
    }

    public void setUnbindingDate(LocalDateTime unbindingDate) {
        this.unbindingDate = unbindingDate;
    }

}