package at.tuwien.lsdc.interfaces;

import java.io.Serializable;
import java.util.Map;
import java.util.SortedMap;

/**
 * Value object to transmit a Message from the Monitor
 * 
 * @author Florian Motlik
 */
public interface MonitorMessage extends Serializable {

    
    /**
     * The history of the message that shows how it was propagated through the system.
     * @return A map where the keys are timestamps (unix time - ms since 1980) and values are the topics for which the message was processed at that moment.
     */
    SortedMap<Long, String> getHistory();

    /**
     * Gets the message object wrapped into the MonitorMessage.
     * @return serializable message object
     */
    Serializable getMessageObject();

    /**
     * Adds topic to the history.
     * @param topic name of the topic
     */
    void addToHistory(String topic);
}
