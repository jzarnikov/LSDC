package at.tuwien.lsdc.performance;

import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

public class DummyMessage implements Serializable {
	
	private static final long serialVersionUID = -4666953341906529897L;

	private Date timestamp;
	
	private Properties properties;
	
	private byte[] data = new byte[1024];
	
	public DummyMessage() {
		timestamp = new Date();
		properties = new Properties();
		properties.put("key1", Math.random());
		properties.put("key2", Math.random());
		properties.put("key3", Math.random());
		properties.put("key4", Math.random());
	}
	
	public String toString() {
		return "DummyMessage from " + timestamp.toString();
	}

}
