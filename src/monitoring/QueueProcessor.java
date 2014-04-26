package monitoring;

import java.util.concurrent.LinkedBlockingDeque;

import analysis.Analysis;
import monitoring.util.FacadeFactory;
import monitoring.util.ResourceAdapter;
import monitoring.util.ResourceFactory;
import database.facade.VirtualMachineFacade;
import database.model.DataCenter;
import database.model.Resource;
import database.model.VirtualMachine;

public class QueueProcessor extends Thread {

	private LinkedBlockingDeque<Message> incoming;
	private Analysis analysis;
	boolean changes = false;

	public QueueProcessor() {
		this.incoming = new LinkedBlockingDeque<Message>();
		this.analysis = new Analysis();
	}

	@Override
	public void run() {
		while (true) {
			while (!incoming.isEmpty()) {
				Message message = incoming.pollFirst();
				writeMessageToDB(message);
				changes = true;
			}
			if (changes) {
				this.analysis.startAnalysis();
				changes = false;
			}
		}
	}

	private void writeMessageToDB(Message message) {

		Resource resource = ResourceFactory.create(message.getType());
		FacadeFactory facadeFactory = new FacadeFactory();
		resource.setID(Integer.parseInt(message.getId()));

		ResourceAdapter.updateResource(resource);

		if (resource instanceof VirtualMachine) {
			VirtualMachineFacade virtualMachineFacade = facadeFactory
					.createVirtualMachineFacade();

			DataCenter dataCenter = facadeFactory.createDataCenterFacade().findAll().get(0);
			((VirtualMachine) resource).setDataCenter(dataCenter);
			virtualMachineFacade.save((VirtualMachine) resource);
		}
		//
		// if(resource instanceof Server){
		// ServerFacade serverFacade = facadeFactory.createServerFacade();
		// serverFacade.save((Server)resource);
		// }

	}

	public synchronized void addTOQueue(Message message) {
		this.incoming.add(message);
	}
}
