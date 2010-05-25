package at.tuwien.lsdc.xml;

import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import at.tuwien.lsdc.xml.generated.HierarchyType;
import at.tuwien.lsdc.xml.generated.TopicType;

public class XMLHierarchyFactory {
	
	public static XMLHierarchy load(File f) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(HierarchyType.class, TopicType.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		HierarchyType root = (HierarchyType) unmarshaller.unmarshal(f);
		return new XMLHierarchy(root);
	}
	
	public static XMLHierarchy load(URL url) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(HierarchyType.class, TopicType.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		HierarchyType root = (HierarchyType) unmarshaller.unmarshal(url);
		return new XMLHierarchy(root);
	}

}
