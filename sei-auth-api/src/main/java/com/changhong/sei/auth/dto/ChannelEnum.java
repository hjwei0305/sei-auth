package com.changhong.sei.auth.dto;

import com.changhong.sei.annotation.Remark;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-09-03 17:23
 */
public enum ChannelEnum {

    /**
     * SEI
     */
    @Remark("SEI")
    SEI,

    /**
     * 手机
     */
    @Remark("手机")
    Mobile,

    /**
     * 邮箱
     */
    @Remark("邮箱")
    EMAIL,

    /**
     * 企业微信
     */
    @Remark("企业微信")
    WeChat,

    /**
     * 钉钉
     */
    @Remark("钉钉")
    DingTalk,

    /**
     * 企业微信
     */
    @Remark("微信小程序")
    WXMiniProgram
}
