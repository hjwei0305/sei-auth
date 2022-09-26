package com.changhong.sei.auth.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Joe
 * @date 2022/4/25
 */
@EqualsAndHashCode(callSuper=false)
@Data
public class EipMailDto {

    /**
     * 账号
     */
    private String account;

    /**
     * 邮件实体
     */
    private String mailBody;

    /**
     * 邮件ID
     */
    private String mailID;

    /**
     * 邮件标题
     */
    private String mailSubject;

    /**
     * 邮件类型
     */
    private String mailType;

    /**
     * url
     */
    private String url;
}
