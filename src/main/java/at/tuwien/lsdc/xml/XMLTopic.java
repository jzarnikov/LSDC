package at.tuwien.lsdc.xml;

import java.util.List;

public class XMLTopic {
	
	private XMLTopic parent;
	
	private List<XMLTopic> children;
	
	private String name;

	public XMLTopic(XMLTopic parent, String name) {
		super();
		this.parent = parent;
		this.name = name;
	}
	
	public XMLTopic getParent() {
		return parent;
	}

	public List<XMLTopic> getChildren() {
		return children;
	}

	public String getName() {
		return name;
	}

	
	
}
