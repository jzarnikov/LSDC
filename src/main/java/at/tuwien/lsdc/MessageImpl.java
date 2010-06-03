package at.tuwien.lsdc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.tuwien.lsdc.interfaces.MonitorMessage;

public class MessageImpl implements MonitorMessage {

    private List<String> history = new ArrayList<String>();

    private final Serializable messageObject;

    public MessageImpl(Serializable messageObject) {
        super();
        this.messageObject = messageObject;
    }

    @Override
    public List<String> getHistory() {
        return Collections.unmodifiableList(this.history);
    }

    @Override
    public Serializable getMessageObject() {
        return this.messageObject;
    }

    @Override
    public void addToHistory(String string) {
        this.history.add(string);
    }

}
