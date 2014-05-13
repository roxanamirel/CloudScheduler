package cleaning;

import java.util.List;

import services.VMService;
import util.ServerState;

import logger.CloudLogger;
import models.VMModel;
import monitoring.util.FacadeFactory;
import database.facade.ServerFacade;
import database.facade.VirtualMachineFacade;
import database.model.Server;
import database.model.VirtualMachine;
import enums.ServiceType;
import exceptions.ServiceCenterAccessException;
import factory.CloudManagerFactory;

public class Cleaner {

	public static void main(String[] args) {
		CleanAllVirtualMachines();
		SetServersToON();
		System.out.println("Done");
	}

	private static void CleanAllVirtualMachines() {
		System.out.println("Cleaning vms...");

		VMService vmService = (VMService) CloudManagerFactory
				.getService(ServiceType.VM);

		VirtualMachineFacade facade = new FacadeFactory()
				.createVirtualMachineFacade();
		List<VirtualMachine> vms = facade.findAll();

		for (VirtualMachine virtualMachine : vms) {
			facade.delete(virtualMachine);
			try {
				VMModel vmModel = vmService.getById(virtualMachine.getID());
				vmService.delete(vmModel);
			} catch (ServiceCenterAccessException e) {
				CloudLogger.getInstance().LogInfo(e.getMessage());
			}
		}
	}

	private static void SetServersToON() {
		System.out.println("Setting servers to " + ServerState.ON.toString());
		ServerFacade facade = new FacadeFactory().createServerFacade();
		List<Server> servers = facade.findAll();

		for (Server server : servers) {
			server.setState(ServerState.ON.toString());
			facade.update(server);
		}
	}
}
