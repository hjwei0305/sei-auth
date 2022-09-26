
package com.changhong.sei.auth.webservice.eipMall;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>AddNoticeType complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="AddNoticeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Account" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Address_BCC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Address_CC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ApprovalDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="ApprovalMan" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CreateDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="CreateMan" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MailBody" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MailID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MailSubject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MailType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SystemName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SystemSort" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Url" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddNoticeType", propOrder = {
    "account",
    "address",
    "addressBCC",
    "addressCC",
    "approvalDate",
    "approvalMan",
    "createDate",
    "createMan",
    "mailBody",
    "mailID",
    "mailSubject",
    "mailType",
    "systemName",
    "systemSort",
    "url"
})
public class AddNoticeType {

    @XmlElement(name = "Account")
    protected String account;
    @XmlElement(name = "Address")
    protected String address;
    @XmlElement(name = "Address_BCC")
    protected String addressBCC;
    @XmlElement(name = "Address_CC")
    protected String addressCC;
    @XmlElementRef(name = "ApprovalDate", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> approvalDate;
    @XmlElement(name = "ApprovalMan")
    protected String approvalMan;
    @XmlElementRef(name = "CreateDate", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> createDate;
    @XmlElement(name = "CreateMan")
    protected String createMan;
    @XmlElement(name = "MailBody")
    protected String mailBody;
    @XmlElement(name = "MailID", required = true)
    protected String mailID;
    @XmlElement(name = "MailSubject")
    protected String mailSubject;
    @XmlElement(name = "MailType")
    protected String mailType;
    @XmlElement(name = "SystemName")
    protected String systemName;
    @XmlElement(name = "SystemSort")
    protected String systemSort;
    @XmlElement(name = "Url")
    protected String url;

    /**
     * ��ȡaccount���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAccount() {
        return account;
    }

    /**
     * ����account���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAccount(String value) {
        this.account = value;
    }

    /**
     * ��ȡaddress���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAddress() {
        return address;
    }

    /**
     * ����address���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * ��ȡaddressBCC���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAddressBCC() {
        return addressBCC;
    }

    /**
     * ����addressBCC���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAddressBCC(String value) {
        this.addressBCC = value;
    }

    /**
     * ��ȡaddressCC���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAddressCC() {
        return addressCC;
    }

    /**
     * ����addressCC���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAddressCC(String value) {
        this.addressCC = value;
    }

    /**
     * ��ȡapprovalDate���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *
     */
    public JAXBElement<XMLGregorianCalendar> getApprovalDate() {
        return approvalDate;
    }

    /**
     * ����approvalDate���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *
     */
    public void setApprovalDate(JAXBElement<XMLGregorianCalendar> value) {
        this.approvalDate = value;
    }

    /**
     * ��ȡapprovalMan���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getApprovalMan() {
        return approvalMan;
    }

    /**
     * ����approvalMan���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setApprovalMan(String value) {
        this.approvalMan = value;
    }

    /**
     * ��ȡcreateDate���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *
     */
    public JAXBElement<XMLGregorianCalendar> getCreateDate() {
        return createDate;
    }

    /**
     * ����createDate���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *
     */
    public void setCreateDate(JAXBElement<XMLGregorianCalendar> value) {
        this.createDate = value;
    }

    /**
     * ��ȡcreateMan���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCreateMan() {
        return createMan;
    }

    /**
     * ����createMan���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCreateMan(String value) {
        this.createMan = value;
    }

    /**
     * ��ȡmailBody���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMailBody() {
        return mailBody;
    }

    /**
     * ����mailBody���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMailBody(String value) {
        this.mailBody = value;
    }

    /**
     * ��ȡmailID���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMailID() {
        return mailID;
    }

    /**
     * ����mailID���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMailID(String value) {
        this.mailID = value;
    }

    /**
     * ��ȡmailSubject���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMailSubject() {
        return mailSubject;
    }

    /**
     * ����mailSubject���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMailSubject(String value) {
        this.mailSubject = value;
    }

    /**
     * ��ȡmailType���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMailType() {
        return mailType;
    }

    /**
     * ����mailType���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMailType(String value) {
        this.mailType = value;
    }

    /**
     * ��ȡsystemName���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSystemName() {
        return systemName;
    }

    /**
     * ����systemName���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSystemName(String value) {
        this.systemName = value;
    }

    /**
     * ��ȡsystemSort���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSystemSort() {
        return systemSort;
    }

    /**
     * ����systemSort���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSystemSort(String value) {
        this.systemSort = value;
    }

    /**
     * ��ȡurl���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUrl() {
        return url;
    }

    /**
     * ����url���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUrl(String value) {
        this.url = value;
    }

}
