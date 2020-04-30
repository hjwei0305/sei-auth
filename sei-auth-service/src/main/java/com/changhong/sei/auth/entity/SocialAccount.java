package com.changhong.sei.auth.entity;

import com.changhong.sei.core.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 实现功能：社交账户
 * 验证数据的合法性:
 * 判断SocialAccount中是否存在该openid的数据。
 * 若存在，直接进行登录。
 * 若不存在，将数据，存储到SocialAccount中，引导用户绑定SEI平台账号。
 * 若本站已存在账号，直接关联账号即可。
 * 若本站不存在账号，引导用户注册，成功后与当前openid关联即可
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-23 18:21
 */
@Entity
@Table(name = "social_account")
@DynamicInsert
@DynamicUpdate
public class SocialAccount extends BaseEntity {
    private static final long serialVersionUID = 1L;
    public static final String FIELD_CHANNEL_CODE = "channelCode";
    public static final String FIELD_OPEN_ID = "openId";

    /**
     * 租户代码
     *
     * @see Account#id
     */
    @Column(name = "tenant_code", length = 100, nullable = false)
    private String tenantCode;
    /**
     * 账号
     *
     * @see Account#id
     */
    @Column(name = "account", length = 100, nullable = false)
    private String account;
    /**
     * 渠道code
     */
    @Column(name = "channel_code", length = 100, nullable = false)
    private String channelCode;
    /**
     * 社交平台开放ID
     */
    @Column(name = "open_id", length = 100, nullable = false)
    private String openId;
    /**
     * 注册时间
     */
    @Column(name = "since_date", updatable = false)
    private LocalDateTime sinceDate;

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public LocalDateTime getSinceDate() {
        return sinceDate;
    }

    public void setSinceDate(LocalDateTime sinceDate) {
        this.sinceDate = sinceDate;
    }
}
