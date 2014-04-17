package monitoring;

import java.io.PrintWriter;
import java.util.concurrent.LinkedBlockingDeque;

import javax.persistence.EntityManager;

import database.connection.DBConnection;
import database.dao.ServerDAO;
import database.facade.ServerFacade;
import database.facade.ServerFacadeImpl;
import database.model.Server;

public class QueueProcessor extends Thread {

	private LinkedBlockingDeque<Message> incoming;
	private PrintWriter writer;
	private ServerDAO serverDAO;
	private ServerFacade serverFacade;
	private EntityManager em = DBConnection.connect();

	public QueueProcessor(PrintWriter writer) {
		this.incoming = new LinkedBlockingDeque<Message>();
		this.writer = writer;
		this.serverDAO = new ServerDAO(em);
		this.serverFacade = new ServerFacadeImpl(serverDAO);
	}

	@Override
	public void run() {
		writeToFile("Enter run method");
		while (true) {
			while (!incoming.isEmpty()) {
				writeToFile("Size of the queue:" + incoming.size());
				Message message = incoming.pollFirst();
				Server server = new Server();
				server.setRam(Integer.parseInt(message.getId()));
				serverFacade.save(server);
				writeToFile("Queue Processor:" + message.toString());

			}

		}
	}

	private void writeToFile(String message) {
		writer.append(message);
		writer.append("\r\n");

	}

	public synchronized void addTOQueue(Message message) {
		this.incoming.add(message);

	}

}
