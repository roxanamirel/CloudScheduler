package monitoring;

import java.util.concurrent.LinkedBlockingDeque;

import analysis.Analysis;
import logger.CloudLogger;
import monitoring.util.FacadeFactory;
import monitoring.util.ResourceAdapter;
import monitoring.util.ResourceFactory;
import database.model.DataCenter;
import database.model.Resource;
import database.model.Server;
import database.model.VirtualMachine;

public class QueueProcessor extends Thread {

	private LinkedBlockingDeque<Message> incoming;
	private FacadeFactory facadeFactory;
	private Analysis analysis;
	boolean changes = false;

	public QueueProcessor() {
		this.incoming = new LinkedBlockingDeque<Message>();
		this.analysis = new Analysis();
		this.facadeFactory = new FacadeFactory();
	}

	@Override
	public void run() {
		while (true) {
			//CloudLogger.getInstance().LogInfo("Waiting for VM's");
			
			while (!incoming.isEmpty()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					CloudLogger.getInstance().LogInfo(e.getMessage());
				}
				Message message = incoming.pollFirst();
				CloudLogger.getInstance().LogInfo(
						"Processing " + message.toString());
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
		resource.setID(Integer.parseInt(message.getId()));
		ResourceAdapter.updateResource(resource);
		VirtualMachine vm;
		DataCenter dataCenter;
		if (resource instanceof VirtualMachine) {
			switch (message.getCommand()) {
			case CREATE:
				dataCenter = facadeFactory.createDataCenterFacade().findAll()
						.get(0);
				vm = (VirtualMachine) resource;
				vm.setDataCenter(dataCenter);
				facadeFactory.createVirtualMachineFacade().save(vm);
				dataCenter.getVMPool().add(vm);
				dataCenter = facadeFactory.createDataCenterFacade().update(
						dataCenter);
				break;

			case DONE:
				facadeFactory = new FacadeFactory();
				vm = facadeFactory.createVirtualMachineFacade().find(
						resource.getID());
				if (vm != null) {
					Server host = vm.getHost();
					facadeFactory.createVirtualMachineFacade().delete(vm);
					dataCenter = facadeFactory.createDataCenterFacade()
							.findAll().get(0);
					dataCenter.getVMPool().remove(vm);
					dataCenter = facadeFactory.createDataCenterFacade().update(
							dataCenter);
					host.getRunningVMs().remove(vm);
					host = facadeFactory.createServerFacade().update(host);
				}
				break;

			default:
				break;
			}
		}
	}

	public synchronized void addTOQueue(Message message) {
		this.incoming.add(message);
	}
}
