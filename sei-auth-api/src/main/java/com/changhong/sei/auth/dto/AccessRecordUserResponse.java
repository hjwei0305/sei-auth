package com.changhong.sei.auth.dto;

import com.changhong.sei.core.dto.BaseEntityDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 访问记录(AccessRecord)DTO类
 *
 * @author sei
 * @since 2021-01-14 15:57:56
 */
@ApiModel(description = "访问记录DTO")
public class AccessRecordUserResponse extends BaseEntityDto {
    private static final long serialVersionUID = 932573143237651356L;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;
    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    private String userAccount;
    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "数量")
    private long countNum;

    @ApiModelProperty(value = "最近访问时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime accessTime;

    public AccessRecordUserResponse() {
    }

    public AccessRecordUserResponse(String userId, String userAccount, String userName, long countNum, LocalDateTime accessTime) {
        this.userId = userId;
        this.userAccount = userAccount;
        this.userName = userName;
        this.countNum = countNum;
        this.accessTime = accessTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getCountNum() {
        return countNum;
    }

    public void setCountNum(long countNum) {
        this.countNum = countNum;
    }

    public LocalDateTime getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(LocalDateTime accessTime) {
        this.accessTime = accessTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccessRecordUserResponse that = (AccessRecordUserResponse) o;

        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }
}