package at.tuwien.lsdc.interfaces;

import java.io.Serializable;

public interface MonitorMessages {

    void sendMessage(String topic, Serializable message);

    void resendMessage(String topic, MonitorMessage message);
}
