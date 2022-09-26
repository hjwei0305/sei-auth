
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
 *         &lt;element name="SvcHdr" type="{http://www.example.org/DONLIM_ES_AGENCYNOTICEINFOSYNC_086/}SvcHdrType"/>
 *         &lt;element name="AppHdr" type="{http://www.example.org/DONLIM_ES_AGENCYNOTICEINFOSYNC_086/}AppHdrType"/>
 *         &lt;element name="AppBody" type="{http://www.example.org/DONLIM_ES_AGENCYNOTICEINFOSYNC_086/}AppBodyType"/>
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
@XmlRootElement(name = "DONLIM_ES_AGENCYNOTICEINFOSYNC_086")
public class DONLIMESAGENCYNOTICEINFOSYNC086_Type {

    @XmlElement(name = "SvcHdr", required = true)
    protected SvcHdrType svcHdr;
    @XmlElement(name = "AppHdr", required = true)
    protected AppHdrType appHdr;
    @XmlElement(name = "AppBody", required = true)
    protected AppBodyType appBody;

    /**
     * ��ȡsvcHdr���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link SvcHdrType }
     *
     */
    public SvcHdrType getSvcHdr() {
        return svcHdr;
    }

    /**
     * ����svcHdr���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link SvcHdrType }
     *
     */
    public void setSvcHdr(SvcHdrType value) {
        this.svcHdr = value;
    }

    /**
     * ��ȡappHdr���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link AppHdrType }
     *
     */
    public AppHdrType getAppHdr() {
        return appHdr;
    }

    /**
     * ����appHdr���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link AppHdrType }
     *
     */
    public void setAppHdr(AppHdrType value) {
        this.appHdr = value;
    }

    /**
     * ��ȡappBody���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link AppBodyType }
     *
     */
    public AppBodyType getAppBody() {
        return appBody;
    }

    /**
     * ����appBody���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link AppBodyType }
     *
     */
    public void setAppBody(AppBodyType value) {
        this.appBody = value;
    }

}
