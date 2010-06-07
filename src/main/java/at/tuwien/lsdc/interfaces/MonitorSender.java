package at.tuwien.lsdc.interfaces;

import java.io.Serializable;

public interface MonitorSender {

    void sendMessage(String topic, Serializable message);

}
