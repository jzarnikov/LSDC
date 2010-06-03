package at.tuwien.lsdc.interfaces;

public interface Hierarchy {

    /**
     * Return all available topics in the hierarchy.
     * @return
     */
    String[] allTopics();

    /**
     * Returns the name of the parent topic.
     * @param topic an existing topic
     * @return the name of the parent or null if a) topic does not exist or b) topic is the root topic
     */
    String getParentOf(String topic);
}
