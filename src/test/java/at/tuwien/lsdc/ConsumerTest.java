package at.tuwien.lsdc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ConsumerTest implements MessageListener {

    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    private int i = 0;

    /**
     * @param args
     */
    public static void main(String[] args) {
        ConsumerTest c = new ConsumerTest();
        c.init();
        c.consume();
        c.shutdown();

    }

    public void init() {
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_BROKER_URL);

            // Create a Connection
            connection = connectionFactory.createConnection();

            // Create a Session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createTopic("TEST.FOO");

            // Create a MessageConsumer from the Session to the Topic or
            // Queue

            consumer = session.createConsumer(destination);
            consumer.setMessageListener(this);
            connection.start();
        } catch (JMSException e) {
            // Should not happen
            System.out.println("Initialization failed");
            e.printStackTrace();
        }
    }

    public void consume() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("<Hit enter to stop>");
        try {
            in.readLine();
        } catch (IOException e) {
            // Should never happen
            System.out.println("Problem while reading input");
            e.printStackTrace();
        }
    }

    public void shutdown() {
        try {
            consumer.close();
            session.close();
            connection.close();
            System.out.println("Consumer closed");
        } catch (JMSException e) {
            // Should not happen
            System.out.println("Closing resources failed");
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message msg) {
        try {
            if (msg instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) msg;
                System.out.println("Received " + i + ": " + textMessage.getText());
            } else if (msg instanceof ObjectMessage) {
                ObjectMessage objectMessage = (ObjectMessage) msg;
                System.out.println("Received " + i + ": " + objectMessage.getObject().toString());
            }
            i++;
        } catch (JMSException e) {
            // Should not happen
            System.out.println("Processing of msg failed");
            e.printStackTrace();
        }
    }

}
