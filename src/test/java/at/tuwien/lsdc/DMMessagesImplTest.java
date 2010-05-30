package at.tuwien.lsdc;

import java.io.IOException;

import org.junit.Test;
import org.mockito.Mockito;

import at.tuwien.lsdc.interfaces.DMCallback;
import at.tuwien.lsdc.interfaces.Hierarchy;

public class DMMessagesImplTest {

    private String topic1 = "TOPIC1";
    private String topic2 = "TOPIC2";

    @Test
    public void listenForEveryToMessages() throws IOException, InterruptedException {
        DMCallback callback = Mockito.mock(DMCallback.class);
        Mockito.when(callback.messageReceived(Mockito.anyString(), Mockito.any())).thenReturn(false, true, true);
        DMCallback printCallback = new DMCallback() {

            @Override
            public boolean messageReceived(String topic, Object message) {
                System.out.println(topic + " " + message);
                return true;
            }
        };
        Hierarchy hierarchy = Mockito.mock(Hierarchy.class);

        Mockito.when(hierarchy.allTopics()).thenReturn(new String[] {topic1, topic2});
        Mockito.when(hierarchy.getParentOf(topic1)).thenReturn(topic2);
        DMMessagesImpl impl = new DMMessagesImpl(hierarchy);
        impl.register(topic1, callback);
        impl.register(topic1, printCallback);
        impl.register(topic2, callback);
        impl.register(topic2, printCallback);

        MonitorMessagesImpl monitorMessage = new MonitorMessagesImpl();
        monitorMessage.sendMessage(topic1, "Test");
        monitorMessage.sendMessage(topic1, "Test");

        Thread.sleep(1000);
        Mockito.verify(callback, Mockito.times(2)).messageReceived("TOPIC1", "Test");
        Mockito.verify(callback).messageReceived("TOPIC2", "Test");

        impl.shutdown();
    }
}
