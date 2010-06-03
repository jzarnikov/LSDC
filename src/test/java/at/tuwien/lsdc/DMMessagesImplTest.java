package at.tuwien.lsdc;

import java.io.IOException;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import at.tuwien.lsdc.interfaces.DMCallback;
import at.tuwien.lsdc.interfaces.Hierarchy;
import at.tuwien.lsdc.interfaces.MonitorMessage;

public class DMMessagesImplTest {

    private String topic1 = "TOPIC1";
    private String topic2 = "TOPIC2";

    @Test
    public void listenForEveryToMessages() throws IOException, InterruptedException {
        DMCallback callback = Mockito.mock(DMCallback.class);
        Mockito.when(callback.messageReceived(Mockito.anyString(), Mockito.any(MonitorMessage.class))).thenReturn(false, true, true);
        DMCallback printCallback = new DMCallback() {

            @Override
            public boolean messageReceived(String topic, MonitorMessage message) {
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
        Mockito.verify(callback, Mockito.times(2)).messageReceived(Mockito.eq("TOPIC1"), Mockito.argThat(new IsMonitorMessageMatcher()));
        Mockito.verify(callback).messageReceived(Mockito.eq("TOPIC2"), Mockito.argThat(new IsMonitorMessageMatcher()));

        impl.shutdown();
    }
    
    class IsMonitorMessageMatcher extends ArgumentMatcher<MonitorMessage> {

		@Override
		public boolean matches(Object o) {
			
			return MonitorMessage.class.isAssignableFrom(o.getClass());
		}
    	
    }
}
