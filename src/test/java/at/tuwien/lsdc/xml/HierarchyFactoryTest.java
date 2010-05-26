package at.tuwien.lsdc.xml;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;
import at.tuwien.lsdc.interfaces.Hierarchy;


public class HierarchyFactoryTest {
	
	@Test
	public void testXMLHierarchyFactory() throws Exception {
		System.out.println(new File(".").getAbsolutePath());
		Hierarchy hierarchy = XMLHierarchyFactory.load(new File("src/test/java/at/tuwien/lsdc/xml/hierarchyTest1.xml"));
		List<String> topics = Arrays.asList(hierarchy.allTopics());
		assertTrue(topics.contains("rootTopic"));
		assertTrue(topics.contains("network"));
		assertTrue(topics.contains("in-trafic"));
		assertTrue(topics.contains("out-trafic"));
		assertTrue(topics.contains("latency"));
		assertTrue(topics.contains("performance"));
		assertTrue(topics.contains("cpu"));
		assertTrue(topics.contains("load"));
		assertTrue(topics.contains("free-cores"));
		assertEquals("cpu", hierarchy.getParentOf("free-cores"));
		assertEquals("cpu", hierarchy.getParentOf("load"));
		assertEquals("performance", hierarchy.getParentOf("cpu"));
		assertEquals("network", hierarchy.getParentOf("latency"));
		assertEquals("network", hierarchy.getParentOf("in-trafic"));
		assertEquals("network", hierarchy.getParentOf("out-trafic"));
		assertEquals("rootTopic", hierarchy.getParentOf("network"));
		assertEquals("rootTopic", hierarchy.getParentOf("performance"));
		assertEquals(null, hierarchy.getParentOf("rootTopic"));
	}

}
