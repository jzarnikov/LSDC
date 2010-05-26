package at.tuwien.lsdc.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.tuwien.lsdc.interfaces.Hierarchy;
import at.tuwien.lsdc.xml.generated.HierarchyType;
import at.tuwien.lsdc.xml.generated.TopicType;

public class XMLHierarchy implements Hierarchy {
	
	private HierarchyType root;
	
	private List<String> topics;
	
	private Map<String, String> parents;
	
	public XMLHierarchy(HierarchyType root) {
		this.root = root;
		topics = new ArrayList<String>();
		parents = new HashMap<String, String>();
		for(TopicType child : root.getTopic()) {
			topics.add(child.getName());
			parents.put(child.getName(), null);
			process(child);
		}
	}
	
	private void process(TopicType topic) {
		for(TopicType child : topic.getTopic()) {
			topics.add(child.getName());
			parents.put(child.getName(), topic.getName());
			process(child);
		}
	}

	@Override
	public String[] allTopics() {
		return topics.toArray(new String[]{});
	}

	@Override
	public String getParentOf(String topic) {
		return parents.get(topic);
	}
	
	

}
