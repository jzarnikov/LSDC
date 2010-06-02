package at.tuwien.lsdc.interfaces;

import java.io.Serializable;
import java.util.List;

public interface MonitorMessage extends Serializable {

    List<String> getHistory();

    Serializable getMessgeObject();

    void addToHistory(String topic);
}
