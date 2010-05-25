package at.tuwien.lsdc.xml;

import at.tuwien.lsdc.interfaces.Hierarchy;
import at.tuwien.lsdc.xml.generated.HierarchyType;

public class XMLHierarchy implements Hierarchy {
	
	private HierarchyType root;
	
	public XMLHierarchy(HierarchyType root) {
		this.root = root;
	}

	@Override
	public String[] allTopics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentOf(String topic) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
