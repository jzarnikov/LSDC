package at.tuwien.lsdc.interfaces;

public interface DMMessages {

    void register(String Topic, DMCallback callback);

    void shutdown();
}
