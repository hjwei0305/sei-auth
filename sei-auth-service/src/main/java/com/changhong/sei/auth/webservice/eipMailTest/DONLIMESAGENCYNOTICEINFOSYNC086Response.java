
package com.changhong.sei.auth.webservice.eipMailTest;

import javax.xml.bind.annotation.*;


/**
 * <p>anonymous complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SvcHdr" type="{http://www.example.org/DONLIM_ES_AGENCYNOTICEINFOSYNC_086/}SvcHdrTypes"/>
 *         &lt;element name="AppHdr" type="{http://www.example.org/DONLIM_ES_AGENCYNOTICEINFOSYNC_086/}AppHdrTypes"/>
 *         &lt;element name="AppBody" type="{http://www.example.org/DONLIM_ES_AGENCYNOTICEINFOSYNC_086/}AppBodyTypes"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "svcHdr",
    "appHdr",
    "appBody"
})
@XmlRootElement(name = "DONLIM_ES_AGENCYNOTICEINFOSYNC_086Response")
public class DONLIMESAGENCYNOTICEINFOSYNC086Response {

    @XmlElement(name = "SvcHdr", required = true)
    protected SvcHdrTypes svcHdr;
    @XmlElement(name = "AppHdr", required = true)
    protected AppHdrTypes appHdr;
    @XmlElement(name = "AppBody", required = true)
    protected AppBodyTypes appBody;

    /**
     * ��ȡsvcHdr���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link SvcHdrTypes }
     *
     */
    public SvcHdrTypes getSvcHdr() {
        return svcHdr;
    }

    /**
     * ����svcHdr���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link SvcHdrTypes }
     *
     */
    public void setSvcHdr(SvcHdrTypes value) {
        this.svcHdr = value;
    }

    /**
     * ��ȡappHdr���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link AppHdrTypes }
     *
     */
    public AppHdrTypes getAppHdr() {
        return appHdr;
    }

    /**
     * ����appHdr���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link AppHdrTypes }
     *
     */
    public void setAppHdr(AppHdrTypes value) {
        this.appHdr = value;
    }

    /**
     * ��ȡappBody���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link AppBodyTypes }
     *
     */
    public AppBodyTypes getAppBody() {
        return appBody;
    }

    /**
     * ����appBody���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link AppBodyTypes }
     *
     */
    public void setAppBody(AppBodyTypes value) {
        this.appBody = value;
    }

}
