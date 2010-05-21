package at.tuwien.lsdc.interfaces;

public interface Hierarchy {

    String[] allTopics();

    String getParentOf(String topic);
}
