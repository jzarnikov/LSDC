package at.tuwien.lsdc;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.store.amq.AMQPersistenceAdapterFactory;

public class ProducerTest {

	private Session session;
	private MessageProducer producer;
	private Connection connection;
	
	
	private void init() {
		System.out.println("Producer started");
		// Create a ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
                
        try {
	        // Create a Connection
	        connection = connectionFactory.createConnection();
	        connection.start();
	
	        // Create a Session
	        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	
	        // Create the destination (Topic or Queue)
	        Destination destination = session.createTopic("TEST.FOO");
	
	        // Create a MessageProducer from the Session to the Topic or
	        // Queue
	        producer = session.createProducer(destination);
	        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        } catch (JMSException e) {
        	//Should not happen
        	System.out.println("Initialization failed");
        	e.printStackTrace();
        }
	}
	
	private void produce() {
		try {
			// Create a messages
	        String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
	        TextMessage message = session.createTextMessage(text);
	
	        // Tell the producer to send the message
	        for (int i = 0; i < 10; i++) {
	            producer.send(message);
	            System.out.println("Sent message: " + message.hashCode() + " : " + Thread.currentThread().getName() + i);
	            Thread.sleep(2000);
	        }
		} catch (JMSException e) {
			// Should not happen
			System.out.println("Sending message failed");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// Should not happen
			System.out.println("Some issue with the Thread");
			e.printStackTrace();
		}
	}
	
	private void shutdown() {
		try {
			// Clean up
	        producer.close();
	        session.close();
	        connection.close();
	        System.out.println("Producer closed"); 
		} catch (JMSException e) {
			// Should not happen
			System.out.println("Closing ressources failed");
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ProducerTest p = new ProducerTest();
		p.init();
		p.produce();
		p.shutdown();        

	}

}
