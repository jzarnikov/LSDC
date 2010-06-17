package at.tuwien.lsdc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import at.tuwien.lsdc.interfaces.MonitorMessage;

public class MessageImpl implements MonitorMessage {

	private static final long serialVersionUID = -6388600431644606873L;

	private Map<Long, String> history = new HashMap<Long, String>();

    private final Serializable messageObject;

    public MessageImpl(Serializable messageObject) {
        super();
        this.messageObject = messageObject;
    }

    @Override
    public Map<Long, String> getHistory() {
        return Collections.unmodifiableMap(history);
    }

    @Override
    public Serializable getMessageObject() {
        return this.messageObject;
    }

    @Override
    public void addToHistory(String string) {
        this.history.put(System.currentTimeMillis(), string);
    }
    
    public void printTimingDebug() {
    	SortedSet<Long> sortedTimestamps = new TreeSet<Long>(history.keySet());
    	long lastTimestamp = sortedTimestamps.first();
    	for(Long timestamp : sortedTimestamps) {
    		//System.out.print((timestamp - lastTimestamp) + ": " + history.get(timestamp) + "; ");
    		System.out.print(timestamp - lastTimestamp + "; ");
    		lastTimestamp = timestamp;
    	}
    	System.out.println();
    }

}
