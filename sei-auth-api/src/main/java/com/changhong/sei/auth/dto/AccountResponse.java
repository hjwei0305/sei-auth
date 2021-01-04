package com.changhong.sei.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 21:04
 */
@ApiModel(description = "账户信息")
public class AccountResponse extends AccountInfoDto {
    private static final long serialVersionUID = 2974541194405245535L;
    /**
     * id主键
     */
    @ApiModelProperty(notes = "id主键")
    private String id;
    /**
     * 用户id
     */
    @ApiModelProperty(notes = "用户id", required = true)
    @NotBlank
    private String userId;
    /**
     * openId
     */
    @ApiModelProperty(notes = "openId", required = true)
    private String openId;
    ;
    /**
     * 名称
     */
    @ApiModelProperty(notes = "名称", required = true)
    @NotBlank
    private String name;
    /**
     * 来源系统(sei,wechat,dingtalk等)
     */
    @ApiModelProperty(notes = "来源系统", required = true)
    @NotNull
    private ChannelEnum channel = ChannelEnum.SEI;
    /**
     * 冻结
     */
    @ApiModelProperty(notes = "冻结")
    private Boolean frozen = Boolean.FALSE;
    /**
     * 锁定
     */
    @ApiModelProperty(notes = "锁定")
    private Boolean locked = Boolean.FALSE;
    /**
     * 注册时间
     */
    @ApiModelProperty(notes = "注册时间", example = "2020-01-14 22:18:48")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sinceDate;
    /**
     * 截止有效期
     */
    @ApiModelProperty(notes = "截止有效期", example = "2099-01-14")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate accountExpired;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChannelEnum getChannel() {
        return channel;
    }

    public void setChannel(ChannelEnum channel) {
        this.channel = channel;
    }

    public Boolean getFrozen() {
        return frozen;
    }

    public void setFrozen(Boolean frozen) {
        this.frozen = frozen;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public LocalDateTime getSinceDate() {
        return sinceDate;
    }

    public void setSinceDate(LocalDateTime sinceDate) {
        this.sinceDate = sinceDate;
    }

    public LocalDate getAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(LocalDate accountExpired) {
        this.accountExpired = accountExpired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountResponse response = (AccountResponse) o;
        return Objects.equals(tenantCode, response.tenantCode) &&
                Objects.equals(account, response.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenantCode, account);
    }
}
