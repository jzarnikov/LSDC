package at.tuwien.lsdc.interfaces;

public interface DecisionMakerSender {
    
    void resendMessage(String topic, MonitorMessage message);
}
