package com.changhong.sei.auth.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Joe
 * @date 2022/4/25
 */
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

    public void setAccount(String account){ this.account=account;}

    public String getAccount(){return account;}

    public void setMailBody(String mailBody){ this.mailBody=mailBody;}

    public String getMailBody(){return mailBody;}

    public void setMailID(String mailID){ this.mailID=mailID;}

    public String getMailID(){return mailID;}

    public void setMailSubject(String mailSubject){ this.mailSubject=mailSubject;}

    public String getMailSubject(){return mailSubject;}

    public void setMailType(String mailType){ this.mailType=mailType;}

    public String getMailType(){return mailType;}

    public void setUrl(String url){ this.url=url;}

    public String getUrl(){return url;}
}
