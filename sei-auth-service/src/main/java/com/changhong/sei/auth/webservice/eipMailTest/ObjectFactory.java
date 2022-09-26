
package com.changhong.sei.auth.webservice.eipMailTest;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the io.renren.modules.ass.webservice.eipMailTest package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AddNoticeTypeCreateDate_QNAME = new QName("", "CreateDate");
    private final static QName _AddNoticeTypeApprovalDate_QNAME = new QName("", "ApprovalDate");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: io.renren.modules.ass.webservice.eipMailTest
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DONLIMESAGENCYNOTICEINFOSYNC086Response }
     *
     */
    public DONLIMESAGENCYNOTICEINFOSYNC086Response createDONLIMESAGENCYNOTICEINFOSYNC086Response() {
        return new DONLIMESAGENCYNOTICEINFOSYNC086Response();
    }

    /**
     * Create an instance of {@link SvcHdrTypes }
     *
     */
    public SvcHdrTypes createSvcHdrTypes() {
        return new SvcHdrTypes();
    }

    /**
     * Create an instance of {@link AppHdrTypes }
     *
     */
    public AppHdrTypes createAppHdrTypes() {
        return new AppHdrTypes();
    }

    /**
     * Create an instance of {@link AppBodyTypes }
     *
     */
    public AppBodyTypes createAppBodyTypes() {
        return new AppBodyTypes();
    }

    /**
     * Create an instance of {@link DONLIMESAGENCYNOTICEINFOSYNC086_Type }
     *
     */
    public DONLIMESAGENCYNOTICEINFOSYNC086_Type createDONLIMESAGENCYNOTICEINFOSYNC086_Type() {
        return new DONLIMESAGENCYNOTICEINFOSYNC086_Type();
    }

    /**
     * Create an instance of {@link SvcHdrType }
     *
     */
    public SvcHdrType createSvcHdrType() {
        return new SvcHdrType();
    }

    /**
     * Create an instance of {@link AppHdrType }
     *
     */
    public AppHdrType createAppHdrType() {
        return new AppHdrType();
    }

    /**
     * Create an instance of {@link AppBodyType }
     *
     */
    public AppBodyType createAppBodyType() {
        return new AppBodyType();
    }

    /**
     * Create an instance of {@link AddNoticeType }
     *
     */
    public AddNoticeType createAddNoticeType() {
        return new AddNoticeType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "CreateDate", scope = AddNoticeType.class)
    public JAXBElement<XMLGregorianCalendar> createAddNoticeTypeCreateDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_AddNoticeTypeCreateDate_QNAME, XMLGregorianCalendar.class, AddNoticeType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "", name = "ApprovalDate", scope = AddNoticeType.class)
    public JAXBElement<XMLGregorianCalendar> createAddNoticeTypeApprovalDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_AddNoticeTypeApprovalDate_QNAME, XMLGregorianCalendar.class, AddNoticeType.class, value);
    }

}
