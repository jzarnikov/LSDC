package at.tuwien.lsdc.clients;

import java.util.Scanner;

import at.tuwien.lsdc.MonitorMessagesImpl;
import at.tuwien.lsdc.interfaces.MonitorSender;

public class MonitorMessagesClient {

    public static void main(String[] args) {
        MonitorSender sender = new MonitorMessagesImpl();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Topic to send to (quit to exit):");
            String topic = scanner.nextLine();
            if (topic.equals("quit")) {
                break;
            } else {
                System.out.print("Message to send:");
                String message = scanner.nextLine();
                sender.sendMessage(topic, message);
            }
        }
    }
}
