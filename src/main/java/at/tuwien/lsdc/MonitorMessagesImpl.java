package at.tuwien.lsdc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import at.tuwien.lsdc.interfaces.DecisionMakerSender;
import at.tuwien.lsdc.interfaces.MonitorMessage;
import at.tuwien.lsdc.interfaces.MonitorSender;

public class MonitorMessagesImpl implements MonitorSender, DecisionMakerSender {

    private ActiveMQConnectionFactory connectionFactory;

    private Connection connection;

    private Session session;

    private Map<String, MessageProducer> producerCache;

    public MonitorMessagesImpl() {
        try {
            connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producerCache = new HashMap<String, MessageProducer>();
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String topic, Serializable messageObject) {
        MessageImpl objectMessage = new MessageImpl(messageObject);
        objectMessage.addToHistory(topic);
        this.send(topic, objectMessage);
    }

    @Override
    public void resendMessage(String topic, MonitorMessage message) {
        message.addToHistory(topic);
        this.send(topic, message);
    }

    private void send(String topic, Serializable object) {

        try {
            MessageProducer producer;
            if (producerCache.containsKey(topic)) {
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

    public void close() {
        try {
            for (MessageProducer producer : producerCache.values()) {
                producer.close();
            }
            session.close();
            connection.close();
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
