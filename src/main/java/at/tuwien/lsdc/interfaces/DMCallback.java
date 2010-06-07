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
     * @return true if problem can be resolved, false otherwise
     */
    boolean messageReceived(String topic, MonitorMessage message);
}
