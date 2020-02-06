package com.changhong.sei.auth.service.client;


import com.changhong.sei.core.dto.serializer.EnumJsonSerializer;
import com.chonghong.sei.enums.UserAuthorityPolicy;
import com.chonghong.sei.enums.UserType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实现功能: AUTH需要的用户信息
 *
 * @author 王锦光 wangjg
 * @version 2020-01-28 10:58
 */
@ApiModel(description = "认证中心需要的用户信息")
public class UserInformation{
    /**
     * 用户Id
     */
    @ApiModelProperty(value = "用户Id", required = true)
    private String userId;

    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型(enum)")
    @JsonSerialize(using = EnumJsonSerializer.class)
    private UserType userType;

    /**
     * 用户权限策略
     */
    @ApiModelProperty(value = "用户权限策略(enum)")
    @JsonSerialize(using = EnumJsonSerializer.class)
    private UserAuthorityPolicy userAuthorityPolicy;

    /**
     * 语言代码
     */
    @ApiModelProperty(value = "语言代码")
    private String languageCode;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserAuthorityPolicy getUserAuthorityPolicy() {
        return userAuthorityPolicy;
    }

    public void setUserAuthorityPolicy(UserAuthorityPolicy userAuthorityPolicy) {
        this.userAuthorityPolicy = userAuthorityPolicy;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}
