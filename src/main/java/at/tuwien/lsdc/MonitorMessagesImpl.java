package at.tuwien.lsdc;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import at.tuwien.lsdc.interfaces.MonitorMessages;

public class MonitorMessagesImpl implements MonitorMessages {

    @Override
    public void sendMessage(String topic, Serializable messageObject) {
        MessageImpl objectMessage = new MessageImpl(messageObject);
        objectMessage.addToHistory(topic);
        this.send(topic, objectMessage);
    }

    @Override
    public void resendMessage(String topic, at.tuwien.lsdc.interfaces.MonitorMessage message) {
        message.addToHistory(topic);
        this.send(topic, message);
    }

    private void send(String topic, Serializable object) {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
            ActiveMQConnection.DEFAULT_BROKER_URL);

        try {
            // Create a Connection
            final Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createTopic(topic);

            // Create a MessageProducer from the Session to the Topic or
            // Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            Message message = session.createObjectMessage(object);
            producer.send(message);

            // Clean Up
            producer.close();
            session.close();
            connection.close();

        } catch (JMSException e) {
            // Should not happen
            System.out.println("Initialization failed");
            e.printStackTrace();
        }
    }

}
