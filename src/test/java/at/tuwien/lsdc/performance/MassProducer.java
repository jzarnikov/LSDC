package at.tuwien.lsdc.performance;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import at.tuwien.lsdc.MonitorMessagesImpl;
import at.tuwien.lsdc.interfaces.MonitorSender;

public class MassProducer {
	
	private static final int DEFAULT_THREADS = 50;
	
	private static final int DEFAULT_SLEEP = 50;
	
	private static int threads = DEFAULT_THREADS;
	
	private static int sleep = DEFAULT_SLEEP;
	
	private static volatile boolean running = true;
	
	private static AtomicLong msgCounter;
	
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
		if(args.length > 0) {
			threads = Integer.parseInt(args[0]);
		}
		if(args.length > 1) {
			sleep = Integer.parseInt(args[1]);
		}
		System.out.printf("Starting mass producer with %d threads and %d ms sleep", threads, sleep);
		msgCounter = new AtomicLong();
		for(int i = 0; i < threads; i++) {
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
				sender.sendMessage(topic, new DummyMessage());
//				long counter = msgCounter.incrementAndGet();
//				if(counter % 100 == 0) {
//					System.out.println("Messages sent: " + counter);
//				}
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					continue;
				}
			}
		}
		
	}

}
