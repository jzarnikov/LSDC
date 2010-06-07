package at.tuwien.lsdc.performance;

import java.io.IOException;

import at.tuwien.lsdc.MonitorMessagesImpl;
import at.tuwien.lsdc.interfaces.MonitorSender;

public class MassProducer {
	
	private static final int THREADS = 10;
	
	private static final int SLEEP = 100;
	
	private static volatile boolean running = true;
	
	private static final String[] topics = {"topic1-1-1-1", "topic1-1-1-2", "topic1-1-1-3", "topic1-1-2-1", "topic1-1-2-2", "topic1-1-2-3", 
		"topic1-1-3-1", "topic1-1-3-2", "topic1-1-3-3", "topic1-2-1-1", "topic1-2-1-2", "topic1-2-1-3", 
		"topic1-2-2-1", "topic1-2-2-2", "topic1-2-2-3", "topic1-2-3-1", "topic1-2-3-2", "topic1-2-3-3", 
		"topic1-3-1-1", "topic1-3-1-2", "topic1-3-1-3", "topic1-3-2-1", "topic1-3-2-2", "topic1-3-2-3", 
		"topic1-3-3-1", "topic1-3-3-2", "topic1-3-3-3", "topic2-1-1-1", "topic2-1-1-2", "topic2-1-1-3", 
		"topic2-1-2-1", "topic2-1-2-2", "topic2-1-2-3", "topic2-1-3-1", "topic2-1-3-2", "topic2-1-3-3", 
		"topic2-2-1-1", "topic2-2-1-2", "topic2-2-1-3", "topic2-2-2-1", "topic2-2-2-2", "topic2-2-2-3", 
		"topic2-2-3-1", "topic2-2-3-2", "topic2-2-3-3", "topic2-3-1-1", "topic2-3-1-2", "topic2-3-1-3", 
		"topic2-3-2-1", "topic2-3-2-2", "topic2-3-2-3", "topic2-3-3-1", "topic2-3-3-2", "topic2-3-3-3", 
		"topic3-1-1-1", "topic3-1-1-2", "topic3-1-1-3", "topic3-1-2-1", "topic3-1-2-2", "topic3-1-2-3", 
		"topic3-1-3-1", "topic3-1-3-2", "topic3-1-3-3", "topic3-2-1-1", "topic3-2-1-2", "topic3-2-1-3", 
		"topic3-2-2-1", "topic3-2-2-2", "topic3-2-2-3", "topic3-2-3-1", "topic3-2-3-2", "topic3-2-3-3", 
		"topic3-3-1-1", "topic3-3-1-2", "topic3-3-1-3", "topic3-3-2-1", "topic3-3-2-2", "topic3-3-2-3", 
		"topic3-3-3-1", "topic3-3-3-2", "topic3-3-3-3"};
	
	public static void main(String[] args) throws IOException {
		for(int i = 0; i < THREADS; i++) {
			ProducerWorker worker = new ProducerWorker(topics[i%topics.length]);
			new Thread(worker).start();
		}
		System.in.read();
	}
	
	private static class ProducerWorker implements Runnable {
		
		private MonitorSender sender = new MonitorMessagesImpl();
		
		private String topic;
		
		public ProducerWorker(String topic) {
			this.topic = topic;
		}

		@Override
		public void run() {
			while(running) {
				MonitorSender sender = new MonitorMessagesImpl();
				sender.sendMessage(topic, new DummyMessage());
				try {
					Thread.sleep(SLEEP);
				} catch (InterruptedException e) {
					continue;
				}
			}
		}
		
	}

}
