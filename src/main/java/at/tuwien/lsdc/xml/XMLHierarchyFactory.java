package at.tuwien.lsdc.xml;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import at.tuwien.lsdc.xml.generated.HierarchyType;
import at.tuwien.lsdc.xml.generated.TopicType;

public class XMLHierarchyFactory {
	
	public static XMLHierarchy load(File f) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance("at.tuwien.lsdc.xml.generated");
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		JAXBElement<HierarchyType> el = (JAXBElement<HierarchyType>) unmarshaller.unmarshal(f);
		HierarchyType root = el.getValue();
		return new XMLHierarchy(root);
	}
	
	public static XMLHierarchy load(URL url) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance("at.tuwien.lsdc.xml.generated");
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		JAXBElement<HierarchyType> el = (JAXBElement<HierarchyType>) unmarshaller.unmarshal(url);
		HierarchyType root = el.getValue();
		return new XMLHierarchy(root);
	}
	
	public static XMLHierarchy load(InputStream stream) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance("at.tuwien.lsdc.xml.generated");
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		JAXBElement<HierarchyType> el = (JAXBElement<HierarchyType>) unmarshaller.unmarshal(stream);
		HierarchyType root = el.getValue();
		return new XMLHierarchy(root);
	}

}
