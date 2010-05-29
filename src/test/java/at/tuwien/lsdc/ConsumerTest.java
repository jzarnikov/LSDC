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
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

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
	        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?useJmx=false");
	
	        // Create a Connection
	        connection = connectionFactory.createConnection();
	
	        // Create a Session
	        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	
	        InitialContext ctx = new InitialContext();
	        Topic destination = (Topic)ctx.lookup("TEST.FOO");
	        	        
	        // Create the destination (Topic or Queue)
	        //Destination destination = session.createTopic("TEST.FOO");
	
	        // Create a MessageConsumer from the Session to the Topic or
	        // Queue
	        consumer = session.createConsumer(destination);
	        consumer.setMessageListener(this);
	        connection.start();
		} catch(JMSException e) {
			// Should not happen
			System.out.println("Initialization failed");
			e.printStackTrace();
	    } catch (NamingException e) {
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
	    } catch(JMSException e) {
	    	// Should not happen
	    	System.out.println("Closing resources failed");
	    	e.printStackTrace();
	    }
	}

	@Override
	public void onMessage(Message msg) {
		TextMessage textMessage = (TextMessage) msg;
        try {
            System.out.println("Received " + i + ": " + textMessage.getText());
            i++;
        } catch (JMSException e) {
            // Should not happen
        	System.out.println("Processing of msg failed");
            e.printStackTrace();
        }
	}

}
