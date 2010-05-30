package at.tuwien.lsdc;

import org.junit.Test;

public class MonitorMessagesImplTest {

    @Test
    public void sendMessage() {
        MonitorMessagesImpl monitorMessage = new MonitorMessagesImpl();
        monitorMessage.sendMessage("TEST.FOO", 1);
    }

}
