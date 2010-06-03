package at.tuwien.lsdc.clients;

import java.io.File;
import java.util.Random;

import javax.xml.bind.JAXBException;

import at.tuwien.lsdc.DMMessagesImpl;
import at.tuwien.lsdc.interfaces.DMCallback;
import at.tuwien.lsdc.interfaces.Hierarchy;
import at.tuwien.lsdc.interfaces.MonitorMessage;
import at.tuwien.lsdc.xml.XMLHierarchyFactory;

public class DecisionMakerClient {
    public static void main(String[] args) throws JAXBException {
        Hierarchy hierarchy = XMLHierarchyFactory.load(new File("src/test/java/at/tuwien/lsdc/xml/hierarchyTest1.xml"));
        DMMessagesImpl impl = new DMMessagesImpl(hierarchy);
        DMCallback callback = new DMCallback() {

            @Override
            public boolean messageReceived(String topic, MonitorMessage message) {
                boolean canHandle = new Random().nextBoolean();
                if (canHandle) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("-------------------------------------\n");
                    builder.append("History: ");
                    for (String string : message.getHistory()) {
                        builder.append(" " + string);
                    }
                    builder.append("\n");
                    builder.append("Object: " + message.getMessageObject() + "\n");

                    builder.append("can Handle: " + canHandle + "\n");
                    builder.append("-------------------------------------\n");
                    System.out.print(builder.toString());
                    System.out.flush();
                }
                return canHandle;
            }
        };

        for (String topic : hierarchy.allTopics()) {
            impl.register(topic, callback);
        }
        Runtime.getRuntime().addShutdownHook(new ShutdownDMMessages(impl));
        while (true)
            ;
    }

    private static class ShutdownDMMessages extends Thread {
        private final DMMessagesImpl messages;

        public ShutdownDMMessages(DMMessagesImpl messages) {
            super();
            this.messages = messages;
        }

        @Override
        public void run() {
            System.out.println("Shutting down");
            System.out.flush();
            this.messages.shutdown();
        }
    }
}
