package execution;

import java.util.List;

import database.facade.ServerFacade;
import database.facade.VirtualMachineFacade;
import database.model.DataCenter;
import database.model.Server;
import database.model.VirtualMachine;

import os.ServerOperations;

import logger.CloudLogger;
import models.ServerModel;
import models.VMModel;
import monitoring.util.FacadeFactory;

import enums.ServiceType;
import exceptions.ServiceCenterAccessException;
import factory.CloudManagerFactory;
import planning.actions.Action;
import planning.actions.Deploy;
import planning.actions.Migrate;
import planning.actions.TurnOffServer;
import planning.actions.TurnOnServer;
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
		FacadeFactory facadeFactory = finalActions.get(0).getFacadeFactory();
		VirtualMachineFacade vmFacade =  facadeFactory.createVirtualMachineFacade();
		ServerFacade serverFacade = facadeFactory.createServerFacade();
		
		for(Action action : finalActions) {
			dataCenter = action.Do(dataCenter);
			if (action instanceof Deploy) {
				for(VirtualMachine vm:dataCenter.getVMPool()){
					if(vm.getID() == action.getVM().getID()){
						vmFacade.update(vm);
						break;
					}
				}
				
				for(Server server:dataCenter.getServerPool()){
					if(server.getID() == action.getDestinationServer().getID() ){
						serverFacade.update(server);
						break;
					}
				}
				
			}
			if (action instanceof Migrate) {
				for(VirtualMachine vm:dataCenter.getVMPool()){
					if(vm.getID() == action.getVM().getID()){
						vmFacade.update(vm);
						break;
					}
				}
				for(Server server:dataCenter.getServerPool()){
					if(server.getID() == action.getDestinationServer().getID() ){
						serverFacade.update(server);
						break;
					}
					if(server.getID() == action.getSourceServer().getID() ){
						serverFacade.update(server);
						break;
					}
				}
			}
			if (action instanceof TurnOffServer) {
				for(Server server:dataCenter.getServerPool()){
					if(server.getID() == action.getSourceServer().getID() ){
						serverFacade.update(server);
						break;
					}
				}
			}
			if (action instanceof TurnOnServer) {
				for(Server server:dataCenter.getServerPool()){
					if(server.getID() == action.getSourceServer().getID() ){
						serverFacade.update(server);
						break;
					}
				}
			}
		}		
		return dataCenter;
	}

	public void executeActions(List<Action> listAction) {
		for (Action action : listAction) {
			execute(action);
		}
	}

	@SuppressWarnings("static-access")
	private void execute(Action action) {
		ServerModel destServerModel = null;
		ServerModel sourceServerModel = null;
		VMModel vmModel = null;
		CloudLogger.getInstance().LogInfo(
				"Executing " + action.toString());
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
