
package com.changhong.sei.auth.webservice.finEipTodoList;

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
 *         &lt;element name="SvcHdr" type="{http://www.example.org/DONLIM_EIP_QUERYTODOLISTSYNC_648/}SvcHdrType"/>
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
    "svcHdr"
})
@XmlRootElement(name = "DONLIM_EIP_QUERYTODOLISTSYNC_648")
public class DONLIMEIPQUERYTODOLISTSYNC648_Type {

    @XmlElement(name = "SvcHdr", required = true)
    protected SvcHdrType svcHdr;

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

}
