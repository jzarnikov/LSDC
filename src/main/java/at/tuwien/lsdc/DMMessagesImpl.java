package at.tuwien.lsdc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import at.tuwien.lsdc.interfaces.DMCallback;
import at.tuwien.lsdc.interfaces.DMMessages;
import at.tuwien.lsdc.interfaces.Hierarchy;

public class DMMessagesImpl implements DMMessages, ExceptionListener, MessageListener {

    private Map<String, List<DMCallback>> callbacks = new HashMap<String, List<DMCallback>>();

    public DMMessagesImpl(Hierarchy hierarchy) {
        String[] allTopics = hierarchy.allTopics();
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

            // Create a Connection
            Connection connection = connectionFactory.createConnection();

            connection.setExceptionListener(this);

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)

            // Create a MessageConsumer from the Session to the Topic or
            // Queue
            for (String topic : allTopics) {
                Destination destination = session.createTopic(topic);
                MessageConsumer consumer = session.createConsumer(destination);
                consumer.setMessageListener(this);
            }
            connection.start();
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    public void register(String topic, DMCallback callback) {
        if (!this.callbacks.containsKey(topic)) {
            initializeMapForTopic(topic);
        }
        List<DMCallback> callbacks = this.callbacks.get(topic);
        callbacks.add(callback);
    }

    private void initializeMapForTopic(String topic) {
        this.callbacks.put(topic, new ArrayList<DMCallback>());
    }

    @Override
    public void onException(JMSException arg0) {
        System.out.println(arg0);
    }

    @Override
    public void onMessage(Message message) {
        System.out.println(message);
    }
}
