//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.05.26 at 02:01:10 AM CEST 
//


package at.tuwien.lsdc.xml.generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the at.tuwien.lsdc.xml.generated package. 
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

    private final static QName _Hierarchy_QNAME = new QName("http://www.infosys.tuwien.ac.at/lsds/hierarchy/", "hierarchy");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: at.tuwien.lsdc.xml.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link HierarchyType }
     * 
     */
    public HierarchyType createHierarchyType() {
        return new HierarchyType();
    }

    /**
     * Create an instance of {@link TopicType }
     * 
     */
    public TopicType createTopicType() {
        return new TopicType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HierarchyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.infosys.tuwien.ac.at/lsds/hierarchy/", name = "hierarchy")
    public JAXBElement<HierarchyType> createHierarchy(HierarchyType value) {
        return new JAXBElement<HierarchyType>(_Hierarchy_QNAME, HierarchyType.class, null, value);
    }

}
