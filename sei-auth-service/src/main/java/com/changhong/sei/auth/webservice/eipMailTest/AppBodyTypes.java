
package com.changhong.sei.auth.webservice.eipMailTest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>AppBodyTypes complex type�� Java �ࡣ
 *
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 *
 * <pre>
 * &lt;complexType name="AppBodyTypes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="User01" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="User02" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="User03" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="User04" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="User05" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Return_Int" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AppBodyTypes", propOrder = {
        "user01",
        "user02",
        "user03",
        "user04",
        "user05",
        "returnInt"
})
public class AppBodyTypes {

    @XmlElement(name = "User01", required = true)
    protected String user01;
    @XmlElement(name = "User02", required = true)
    protected String user02;
    @XmlElement(name = "User03", required = true)
    protected String user03;
    @XmlElement(name = "User04", required = true)
    protected String user04;
    @XmlElement(name = "User05", required = true)
    protected String user05;
    @XmlElement(name = "Return_Int")
    protected Integer returnInt;

    /**
     * ��ȡuser01���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUser01() {
        return user01;
    }

    /**
     * ����user01���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUser01(String value) {
        this.user01 = value;
    }

    /**
     * ��ȡuser02���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUser02() {
        return user02;
    }

    /**
     * ����user02���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUser02(String value) {
        this.user02 = value;
    }

    /**
     * ��ȡuser03���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUser03() {
        return user03;
    }

    /**
     * ����user03���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUser03(String value) {
        this.user03 = value;
    }

    /**
     * ��ȡuser04���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUser04() {
        return user04;
    }

    /**
     * ����user04���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUser04(String value) {
        this.user04 = value;
    }

    /**
     * ��ȡuser05���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUser05() {
        return user05;
    }

    /**
     * ����user05���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUser05(String value) {
        this.user05 = value;
    }

    /**
     * ��ȡreturnInt���Ե�ֵ��
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getReturnInt() {
        return returnInt;
    }

    /**
     * ����returnInt���Ե�ֵ��
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setReturnInt(Integer value) {
        this.returnInt = value;
    }

}
