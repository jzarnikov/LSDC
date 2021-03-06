package at.tuwien.lsdc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import at.tuwien.lsdc.interfaces.DMCallback;
import at.tuwien.lsdc.interfaces.DMMessages;
import at.tuwien.lsdc.interfaces.DecisionMakerSender;
import at.tuwien.lsdc.interfaces.Hierarchy;
import at.tuwien.lsdc.interfaces.MonitorMessage;

public class DMMessagesImpl implements DMMessages, ExceptionListener {

    /**
     * Mapping from a Topic to a Listener.
     */
    private Map<String, Listener> listeners = Collections.synchronizedMap(new HashMap<String, Listener>());

    private List<MessageConsumer> consumers = new ArrayList<MessageConsumer>();

    /**
     * To resend a specific MonitorMessage
     */
    private DecisionMakerSender sender = new MonitorMessagesImpl();
    
    // Stored to being able to close later on
    private Connection connection;
    private Session session;

    public DMMessagesImpl(Hierarchy hierarchy) {
        String[] allTopics = hierarchy.allTopics();
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_BROKER_URL);

            // Create a Connection
            connection = connectionFactory.createConnection();

            // Create a Session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            ExecutorService executorService = Executors.newCachedThreadPool();

            for (String topic : allTopics) {
                // Create the destination (Topic or Queue)
                Destination destination = session.createTopic(topic);

                // Create a MessageConsumer from the Session to the Topic or
                // Queue
                MessageConsumer consumer = session.createConsumer(destination);
                // For each topic a listener is created.
                Listener listener = new Listener(topic, this.sender, hierarchy, executorService);
                // The message Listener has to be set for every MessageConsumer.
                // Has to implement the MessageListener interface
                consumer.setMessageListener(listener);
                this.listeners.put(topic, listener);
                System.out.println("Added Listener for Topic: " + topic);
            }
            connection.start();
        } catch (JMSException e) {
            // Should not happen
            System.out.println("Initialization failed");
            e.printStackTrace();
        }
    }

    public void register(String topic, DMCallback callback) {
        // If a Listener for a specific topic exists the DMCallback is
        // added to its list of callbacks.
        if (this.listeners.containsKey(topic)) {
            this.listeners.get(topic).addCallback(callback);
        }
    }

    @Override
    public void onException(JMSException arg0) {
        System.out.println(arg0);
    }

    @Override
    public void shutdown() {
        try {
            for (MessageConsumer consumer : this.consumers) {
                consumer.close();
            }
            this.session.close();
            this.connection.close();
        } catch (JMSException e) {
            System.out.println("Couldn't shutdown");
            e.printStackTrace();
        }
    }

    private static class Listener implements MessageListener {

        private List<DMCallback> callbacks = new ArrayList<DMCallback>();

        private final DecisionMakerSender sender;
        
        private ExecutorService executorService;

        private final String topic;

        private Hierarchy hierarchy;

        public Listener(String topic, DecisionMakerSender sender, Hierarchy hierarchy, ExecutorService executorService) {
            super();
            this.topic = topic;
            this.sender = sender;
            this.hierarchy = hierarchy;
            this.executorService = executorService; 
        }

        @Override
        public void onMessage(final Message message) {
        	executorService.submit(new Runnable() {
				
				@Override
				public void run() {
					if (message instanceof ObjectMessage) {
		                ObjectMessage objectMessage = (ObjectMessage) message;
		                callCallbacks(objectMessage);
		            }					
				}
			});
        }

        private void callCallbacks(ObjectMessage objectMessage) {
            for (DMCallback callback : this.callbacks) {
                try {
                    Serializable object = objectMessage.getObject();
                    if (object instanceof at.tuwien.lsdc.interfaces.MonitorMessage) {
                        // Only MonitorMessages should be used
                        checkCallback(callback, object);
                    }
                } catch (JMSException e) {
                    System.out.println("Can't call callback");
                    e.printStackTrace();
                }
            }
        }

        private void checkCallback(DMCallback callback, Serializable object) {
            MonitorMessage monitorMessage = (MonitorMessage) object;
            if (!callback.messageReceived(topic, monitorMessage)) {
                // If the callback declines the Message it has to be resent to
                // the parent of the current topic
                String parentOf = hierarchy.getParentOf(this.topic);
                if (parentOf != null) {
                    this.sender.resendMessage(parentOf, monitorMessage);
                }
            }

        }

        public void addCallback(DMCallback callback) {
            this.callbacks.add(callback);
        }
    }

}
