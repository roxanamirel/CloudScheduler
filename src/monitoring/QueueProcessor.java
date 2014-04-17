package monitoring;

import java.io.PrintWriter;
import java.util.concurrent.LinkedBlockingDeque;

public class QueueProcessor extends Thread {

	private LinkedBlockingDeque<Message> incoming;
	private PrintWriter writer;

	public QueueProcessor(LinkedBlockingDeque<Message> incoming,
			PrintWriter writer) {
		this.incoming = incoming;
		this.writer = writer;
	}

	@Override
	public void run() {
		writeToFile("Enter run method");
			while (!incoming.isEmpty()) {
				writeToFile("Size of the queue:" + incoming.size());
				Message message = incoming.pollFirst();
				writeToFile("Queue Processor:" + message.toString());
			}
	}

	private void writeToFile(String message) {
		writer.append(message);
		writer.append("\r\n");

	}

}
