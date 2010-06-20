package at.tuwien.lsdc.interfaces;

import java.io.Serializable;

public interface MonitorSender {

	/**
	 * Sends serializabe message to receiver topic. Wrapping the message may 
	 * be needed.
	 * @param topic name of the topic
	 * @param message message to be sent
	 */
    void sendMessage(String topic, Serializable message);

}
