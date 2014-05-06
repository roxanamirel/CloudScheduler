package execution;

import java.util.List;

import database.model.DataCenter;

import os.ServerOperations;

import logger.CloudLogger;
import models.ServerModel;
import models.VMModel;

import enums.ServiceType;
import exceptions.ServiceCenterAccessException;
import factory.CloudManagerFactory;
import planning.Action;
import planning.Deploy;
import planning.Migrate;
import planning.TurnOffServer;
import planning.TurnOnServer;
import services.ServerService;
import services.VMService;


public class Execution {
	private ServerService serverService;
	private VMService vmService;
	private ServerOperations serverOperations;

	public Execution() {
		this.serverService = (ServerService) CloudManagerFactory
				.getService(ServiceType.SERVER);
		this.vmService = (VMService) CloudManagerFactory
				.getService(ServiceType.VM);
		this.serverOperations = new ServerOperations();

	}

	public DataCenter updateDatabase(DataCenter dataCenter,
			List<Action> finalActions) {
		
		for(Action action : finalActions) {
			
			if (action instanceof Deploy) {
				action.getFacadeFactory().createVirtualMachineFacade().update(action.getVM());
				action.getFacadeFactory().createServerFacade().update(action.getDestinationServer());
			}
			if (action instanceof Migrate) {
				action.getFacadeFactory().createVirtualMachineFacade().update(action.getVM());
				action.getFacadeFactory().createServerFacade().update(action.getDestinationServer());
				action.getFacadeFactory().createServerFacade().update(action.getSourceServer());
			}
			if (action instanceof TurnOffServer) {
				action.getFacadeFactory().createServerFacade().update(action.getSourceServer());
			}
			if (action instanceof TurnOnServer) {
				action.getFacadeFactory().createServerFacade().update(action.getSourceServer());
			}
		}		
		
		return dataCenter;
	}

	public void executeActions(List<Action> listAction) {
		for (Action action : listAction) {
			execute(action);
		}
	}

	private void execute(Action action) {
		ServerModel destServerModel = null;
		ServerModel sourceServerModel = null;
		VMModel vmModel = null;
		CloudLogger.getInstance().LogInfo(
				"Executing " + action.getClass().getName());
		try {
			if (action.getDestinationServer() != null) {
				destServerModel = serverService.getById(action
						.getDestinationServer().getID());
			}
			if (action.getSourceServer() != null) {
				sourceServerModel = serverService.getById(action
						.getSourceServer().getID());
			}
			if (action.getVM() != null) {
				vmModel = vmService.getById(action.getVM().getID());
			}
		} catch (ServiceCenterAccessException e) {
			CloudLogger.getInstance().LogInfo(e.getMessage());
		}

		if (action instanceof Deploy) {
			vmService.deploy(vmModel, destServerModel);
		}
		if (action instanceof Migrate) {
			vmService.migrate(vmModel, destServerModel);
		}
		if (action instanceof TurnOffServer) {
			serverOperations.shutDown(sourceServerModel);
		}
		if (action instanceof TurnOnServer) {
			serverOperations.wakeUp(sourceServerModel);
		}
	}
}
