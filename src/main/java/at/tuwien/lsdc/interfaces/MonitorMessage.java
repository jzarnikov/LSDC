package at.tuwien.lsdc.interfaces;

import java.io.Serializable;
import java.util.List;

/**
 * Value object to transmit a Message from the Monitor
 * 
 * @author Florian Motlik
 */
public interface MonitorMessage extends Serializable {

    /**
     * @return history of the topics the message was sent to
     */
    List<String> getHistory();

    Serializable getMessageObject();

    void addToHistory(String topic);
}
