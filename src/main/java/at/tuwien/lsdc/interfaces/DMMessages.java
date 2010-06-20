package at.tuwien.lsdc.interfaces;

public interface DMMessages {

    /**
     * Register a callback for when a message is received for the specified
     * topic.
     * @param topic name of the topic
     * @param callback callback object to be registered
     */
    void register(String topic, DMCallback callback);

    /**
     * Cleanly shutdown all listeners for the topics.
     */
    void shutdown();
}
