package com.changhong.sei.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-10-21 16:46
 */
@ApiModel(description = "OAuth2认证结果")
public class OAuth2Response implements Serializable {
    private static final long serialVersionUID = -3314619605417123240L;
    /**
     * 会话id
     */
    @ApiModelProperty(notes = "会话id")
    private String sid;
    /**
     * token
     */
    @ApiModelProperty(notes = "会话token")
    private String accessToken;

    @ApiModelProperty(notes = "过期时间")
    private final long expireIn;

    public OAuth2Response(LocalDateTime time) {
        this.expireIn = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpireIn() {
        return expireIn;
    }
}
