package planning;

import java.util.List;

import logger.CloudLogger;
import models.ServerModel;
import models.VMModel;
import monitoring.util.FacadeFactory;
import services.ServerService;
import services.VMService;
import database.model.DataCenter;
import database.model.Server;
import database.model.VirtualMachine;
import enums.ServiceType;
import exceptions.ServiceCenterAccessException;
import factory.CloudManagerFactory;

public class Deploy extends Action {

	public Deploy(Server sourceServerID, Server destinationServerID,
			VirtualMachine vMID) {
		super(sourceServerID, destinationServerID, vMID);
	}

	@Override
	public DataCenter Do(DataCenter dc) {
		VMService vmService = (VMService) CloudManagerFactory
				.getService(ServiceType.VM);
		ServerService serverService = (ServerService) CloudManagerFactory
				.getService(ServiceType.SERVER);
		try {
			VirtualMachine vm = this.getVM();
			VMModel vmModel = vmService.getById(vm.getID());
			ServerModel serverModel = serverService.getById(this
					.getDestinationServer().getID());
			vmModel = vmService.deploy(vmModel, serverModel);
			FacadeFactory facadeFactory = new FacadeFactory();
			Server server = facadeFactory.createServerFacade().find(
					serverModel.getId());
			vm.setHost(server);
			vm = facadeFactory.createVirtualMachineFacade().update(vm);
			this.setVM(vm);
		} catch (ServiceCenterAccessException e) {
			CloudLogger.getInstance().LogInfo(e.getMessage());
		}
		return dc;
	}

	@Override
	public DataCenter Undo(DataCenter dc) {
		// TODO Auto-generated method stub
		return null;
	}
}
