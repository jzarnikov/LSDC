package at.tuwien.lsdc.interfaces;

public interface DMMessages {

    /**
     * Register a callback for when a message is received for the specified
     * topic.
     */
    void register(String topic, DMCallback callback);

    /**
     * Cleanly shutdown all listeners for the topics.
     */
    void shutdown();
}
