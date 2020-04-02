package com.changhong.sei.auth.common.validatecode;

import java.io.Serializable;

/**
 * 实现功能：验证码类
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-03-31 15:28
 */
public class VerifyCode implements Serializable {
    private static final long serialVersionUID = 92055580983937496L;

    private String code;

    private byte[] imgBytes;

    private long expireTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte[] getImgBytes() {
        return imgBytes;
    }

    public void setImgBytes(byte[] imgBytes) {
        this.imgBytes = imgBytes;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
