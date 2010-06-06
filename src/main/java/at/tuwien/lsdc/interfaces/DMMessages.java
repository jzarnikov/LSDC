package at.tuwien.lsdc.interfaces;

public interface DMMessages {

    void register(String topic, DMCallback callback);

    void shutdown();
    
    void resendMessage(String topic, MonitorMessage message);
}
