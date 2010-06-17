package at.tuwien.lsdc.performance;

import java.io.File;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import at.tuwien.lsdc.DMMessagesImpl;
import at.tuwien.lsdc.MessageImpl;
import at.tuwien.lsdc.interfaces.DMCallback;
import at.tuwien.lsdc.interfaces.Hierarchy;
import at.tuwien.lsdc.interfaces.MonitorMessage;
import at.tuwien.lsdc.xml.XMLHierarchyFactory;

public class DummyDecisionMaker {
	
	private static final float HANDLE_PROBABILITY = 0.9f;
	
	private static final AtomicLong counter = new AtomicLong();
	
	public static void main(String[] args) throws Exception {
		Hierarchy hierarchy = XMLHierarchyFactory.load(new File("src/test/java/at/tuwien/lsdc/performance/performanceTestHierarchy.xml"));
        DMMessagesImpl impl = new DMMessagesImpl(hierarchy);
        DMCallback callback = new DMCallback() {

            @Override
            public boolean messageReceived(String topic, MonitorMessage message) {
            	boolean handle = new Random().nextFloat() > HANDLE_PROBABILITY;
            	if(handle) {
            		long counterValue = counter.incrementAndGet();
            		if(counterValue % 512 == 0) {
            			((MessageImpl) message).printTimingDebug();
            		}
            	}
                return handle;
            }
        };

        for (String topic : hierarchy.allTopics()) {
            impl.register(topic, callback);
        }
        Runtime.getRuntime().addShutdownHook(new ShutdownDMMessages(impl));
        System.in.read();
    }

    private static class ShutdownDMMessages extends Thread {
        private final DMMessagesImpl messages;

        public ShutdownDMMessages(DMMessagesImpl messages) {
            this.messages = messages;
        }

        @Override
        public void run() {
            this.messages.shutdown();
        }
    }

}
