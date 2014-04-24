package monitoring;

import java.util.concurrent.LinkedBlockingDeque;
import monitoring.util.FacadeFactory;
import monitoring.util.ResourceAdapter;
import monitoring.util.ResourceFactory;
import database.facade.VirtualMachineFacade;
import database.model.Resource;
import database.model.VirtualMachine;

public class QueueProcessor extends Thread {

	private LinkedBlockingDeque<Message> incoming;	

	public QueueProcessor() {
		this.incoming = new LinkedBlockingDeque<Message>();
	}

	@Override
	public void run() {
		while (true) {
			while (!incoming.isEmpty()) {
				Message message = incoming.pollFirst();
				writeMessageToDB(message);
			}
		}
	}

	private void writeMessageToDB(Message message) {
		
		Resource resource = ResourceFactory.create(message.getType());
		FacadeFactory facadeFactory = new FacadeFactory();		
		resource.setID(Integer.parseInt(message.getId()));
		
		ResourceAdapter.updateResource(resource);
		
		
		//facade.save(resource);
		
		if (resource instanceof VirtualMachine) {
			VirtualMachineFacade virtualMachineFacade = facadeFactory.createVirtualMachineFacade();
			virtualMachineFacade.save((VirtualMachine)resource);
		}
//		
//		if(resource instanceof Server){
//			ServerFacade serverFacade = facadeFactory.createServerFacade();
//			serverFacade.save((Server)resource);
//		}
		
	}

	public synchronized void addTOQueue(Message message) {
		this.incoming.add(message);
	}
}
