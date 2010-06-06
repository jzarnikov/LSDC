package at.tuwien.lsdc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import at.tuwien.lsdc.interfaces.DMCallback;
import at.tuwien.lsdc.interfaces.DMMessages;
import at.tuwien.lsdc.interfaces.Hierarchy;
import at.tuwien.lsdc.interfaces.MonitorMessage;
import at.tuwien.lsdc.interfaces.MonitorMessages;

public class DMMessagesImpl implements DMMessages, ExceptionListener {

    private Map<String, Listener> listeners = Collections.synchronizedMap(new HashMap<String, Listener>());

    private List<MessageConsumer> consumers = new ArrayList<MessageConsumer>();
    
    private Map<String, MessageProducer> producerCache;

    private Connection connection;
    private Session session;

    public DMMessagesImpl(Hierarchy hierarchy) {
        String[] allTopics = hierarchy.allTopics();
        producerCache = new HashMap<String, MessageProducer>();
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_BROKER_URL);

            // Create a Connection
            connection = connectionFactory.createConnection();

            // Create a Session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            for (String topic : allTopics) {
                // Create the destination (Topic or Queue)
                Destination destination = session.createTopic(topic);

                // Create a MessageConsumer from the Session to the Topic or
                // Queue

                MessageConsumer consumer = session.createConsumer(destination);
                Listener listener = new Listener(topic, this, hierarchy);
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
    
    @Override
	public void resendMessage(String topic, MonitorMessage message) {
    	message.addToHistory(topic);
    	this.send(topic, message);		
	}
    
    private void send(String topic, Serializable object) {
        
        try {
            MessageProducer producer;
            if(producerCache.containsKey(topic)) {
            	producer = producerCache.get(topic);
            } else {
            	Destination destination = session.createTopic(topic);
            	producer = session.createProducer(destination);
            	producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            	producerCache.put(topic, producer);
            }

            Message message = session.createObjectMessage(object);
            producer.send(message);

        } catch (JMSException e) {
            // Should not happen
            System.out.println("Initialization failed");
            e.printStackTrace();
        }
    }

    private static class Listener implements MessageListener {

        private List<DMCallback> callbacks = new ArrayList<DMCallback>();

        private final DMMessages sender;

        private final String topic;

        private Hierarchy hierarchy;

        public Listener(String topic, DMMessages sender, Hierarchy hierarchy) {
            super();
            this.topic = topic;
            this.sender = sender;
            this.hierarchy = hierarchy;
        }

        @Override
        public void onMessage(Message message) {
            if (message instanceof ObjectMessage) {
                ObjectMessage objectMessage = (ObjectMessage) message;
                for (DMCallback callback : this.callbacks) {
                    try {
                        Serializable object = objectMessage.getObject();
                        if (object instanceof at.tuwien.lsdc.interfaces.MonitorMessage) {
                        	System.out.println(((MonitorMessage)object).toString());
                            MonitorMessage monitorMessage = (MonitorMessage) object;
                            if (!callback.messageReceived(topic, monitorMessage)) {
                                String parentOf = hierarchy.getParentOf(this.topic);
                                if (parentOf != null) {
                                    this.sender.resendMessage(parentOf, monitorMessage);
                                }
                            }
                        }
                    } catch (JMSException e) {
                        System.out.println("Can't call callback");
                        e.printStackTrace();
                    }
                }
            }
        }

        public void addCallback(DMCallback callback) {
            this.callbacks.add(callback);
        }
    }

}
