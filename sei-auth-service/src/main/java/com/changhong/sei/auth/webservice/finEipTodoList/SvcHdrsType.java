
package com.changhong.sei.auth.webservice.finEipTodoList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SvcHdrsType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="SvcHdrsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RCODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RDESC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ESBCODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="User01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="User02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="User03" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="User04" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="User05" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ResultJson" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SvcHdrsType", propOrder = {
    "rcode",
    "rdesc",
    "esbcode",
    "user01",
    "user02",
    "user03",
    "user04",
    "user05",
    "resultJson"
})
public class SvcHdrsType {

    @XmlElement(name = "RCODE", required = true)
    protected String rcode;
    @XmlElement(name = "RDESC")
    protected String rdesc;
    @XmlElement(name = "ESBCODE")
    protected String esbcode;
    @XmlElement(name = "User01")
    protected String user01;
    @XmlElement(name = "User02")
    protected String user02;
    @XmlElement(name = "User03")
    protected String user03;
    @XmlElement(name = "User04")
    protected String user04;
    @XmlElement(name = "User05")
    protected String user05;
    @XmlElement(name = "ResultJson")
    protected String resultJson;

    /**
     * 获取rcode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRCODE() {
        return rcode;
    }

    /**
     * 设置rcode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRCODE(String value) {
        this.rcode = value;
    }

    /**
     * 获取rdesc属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRDESC() {
        return rdesc;
    }

    /**
     * 设置rdesc属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRDESC(String value) {
        this.rdesc = value;
    }

    /**
     * 获取esbcode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getESBCODE() {
        return esbcode;
    }

    /**
     * 设置esbcode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setESBCODE(String value) {
        this.esbcode = value;
    }

    /**
     * 获取user01属性的值。
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
     * 设置user01属性的值。
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
     * 获取user02属性的值。
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
     * 设置user02属性的值。
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
     * 获取user03属性的值。
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
     * 设置user03属性的值。
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
     * 获取user04属性的值。
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
     * 设置user04属性的值。
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
     * 获取user05属性的值。
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
     * 设置user05属性的值。
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
     * 获取resultJson属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultJson() {
        return resultJson;
    }

    /**
     * 设置resultJson属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultJson(String value) {
        this.resultJson = value;
    }

}
