package at.tuwien.lsdc;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;
import org.mockito.Mockito;

import at.tuwien.lsdc.interfaces.Hierarchy;

public class DMMessagesImplTest {

	private static boolean running = true;
	
    @Test
    public void listenForEveryToMessages() {
        Hierarchy hierarchy = Mockito.mock(Hierarchy.class);
        Mockito.when(hierarchy.allTopics()).thenReturn(new String[] {"TOPIC1", "TOPIC2"});
        DMMessagesImpl impl = new DMMessagesImpl(hierarchy);
        while (true)
            ;
    }

    public static void main(String[] args) throws Exception {
    	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    	thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldProducer(), false);
        in.readLine();
        running = false;
    }

    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    public static class HelloWorldProducer implements Runnable {
        public void run() {
            System.out.println("Producer started");
        	try {
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();

                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Create the destination (Topic or Queue)
                Destination destination = session.createTopic("TEST.FOO");

                // Create a MessageProducer from the Session to the Topic or
                // Queue
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

                // Create a messages
                String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
                TextMessage message = session.createTextMessage(text);

                // Tell the producer to send the message
                System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName());
                for (int i = 0; i < 10; i++) {
                    producer.send(message);
                    Thread.sleep(1000);
                }

                // Clean up
                producer.close();
                session.close();
                connection.close();
                System.out.println("Producer closed");
                System.out.println("<Hit enter to stop>");
            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
        }
    }

    public static class HelloWorldConsumer implements Runnable, ExceptionListener, MessageListener {

        private int i = 0;

        public void run() {
            System.out.println("Consumer started");
        	try {

                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

                // Create a Connection
                Connection connection = connectionFactory.createConnection();

                connection.setExceptionListener(this);

                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Create the destination (Topic or Queue)
                Destination destination = session.createTopic("TEST.FOO");

                // Create a MessageConsumer from the Session to the Topic or
                // Queue
                MessageConsumer consumer = session.createConsumer(destination);
                consumer.setMessageListener(this);
                connection.start();
                while (running)
                    ;
                System.out.println("Consumer closed");
                consumer.close();
                session.close();
                connection.close();
            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            } 
        }

        public synchronized void onException(JMSException ex) {
            System.out.println("JMS Exception occured.  Shutting down client.");
        }

        @Override
        public void onMessage(Message message) {
            TextMessage textMessage = (TextMessage) message;
            try {
                System.out.println("Received " + i + ": " + textMessage.getText());
                i++;
            } catch (JMSException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
