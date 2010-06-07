package at.tuwien.lsdc.interfaces;

/**
 * When the DecisionMaker can't resolve a specific problem the message has to be
 * resent. When resending the MonitorMessage object has to be used again.
 * 
 * @author Florian Motlik
 * 
 */
public interface DecisionMakerSender {

    void resendMessage(String topic, MonitorMessage message);
}
