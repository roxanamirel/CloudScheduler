package monitoring;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.LinkedBlockingDeque;

import javax.jws.WebMethod;
import javax.jws.WebService;

import monitoring.command.Command;
import monitoring.types.Type;

@WebService
public class HookTriggeredWS {

	private LinkedBlockingDeque<Message> incoming;
	private PrintWriter writer;
    private QueueProcessor queueProcessor;
    
	public HookTriggeredWS() {
		this.incoming =  new LinkedBlockingDeque<Message>();
		try {
			writer = new PrintWriter("VM-file-name.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		queueProcessor = new QueueProcessor(incoming,writer);
		
		
//		Thread one = new Thread() {
//			public void run() {
//				while (true) {
//					while (!incoming.isEmpty()) {
//						Message message = incoming.pollFirst();
//						WriteToFile("I entered one thread");
//						WriteToFile(message.toString());
//					}	
//					
//				}
//			}
//		};
//		one.start();
		}
	

	@WebMethod
	public void addMessageToQueue(String id, String command, String type) {
		
		Type typeT = MessageHelper.getType(type);
		Command commandC = MessageHelper.getCommand(command);
		Message message = new Message(id, typeT, commandC);
		writeToFile("WS has been called");
		incoming.offer(message);
		if(!queueProcessor.isAlive()){
			writeToFile("I am starting the thread");
			queueProcessor.run();
		}
		writer.close();
	}
	
	private void writeToFile(String message) {
		      writer.append(message);	  
	          writer.append("\r\n");
	         
	}

}
