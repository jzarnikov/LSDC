package at.tuwien.lsdc.interfaces;

public interface DMCallback {

    boolean messageReceived(String topic, MonitorMessage message);
}
