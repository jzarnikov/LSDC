package at.tuwien.lsdc.interfaces;

/**
 * The DecisionMakers have to implement this Interface to receive a message and
 * tell if the problem can be resolved.
 * 
 * @author Florian Motlik
 * 
 */
public interface DMCallback {

    /**
     * This message is called when callback receives a message from the monitor.
     * @param topic name of the actual topic
     * @param message message received from the monitor
     * @return true if problem can be resolved, false otherwise
     */
    boolean messageReceived(String topic, MonitorMessage message);
}
