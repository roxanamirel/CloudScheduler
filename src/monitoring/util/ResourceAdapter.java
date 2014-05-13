package monitoring.util;

import java.util.ArrayList;
import java.util.List;
import models.ServerModel;
import models.VMModel;
import services.ServerService;
import services.VMService;
import database.model.CPU;
import database.model.CPUCore;
import database.model.RAM;
import database.model.Resource;
import database.model.Server;
import database.model.VirtualMachine;
import enums.ServiceType;
import exceptions.ServiceCenterAccessException;
import factory.CloudManagerFactory;

public class ResourceAdapter {

	public static void updateResource(Resource resource) {
		if (resource instanceof VirtualMachine) {
			VirtualMachine virtualMachine = (VirtualMachine) resource;
			VMService vmService = (VMService) CloudManagerFactory
					.getService(ServiceType.VM);

			try {
				VMModel model = vmService.getById(resource.getID());
				virtualMachine.setName(model.getName());
				virtualMachine.setCPU(constructCPU(model));
				virtualMachine.setRAM(constructRAM(model));
			} catch (ServiceCenterAccessException e) {
				e.printStackTrace();
			}
		} else if (resource instanceof Server) {
			Server server = (Server) resource;
			ServerService serverService = (ServerService) CloudManagerFactory
					.getService(ServiceType.SERVER);
			try {
				ServerModel model = serverService.getById(resource.getID());
			} catch (ServiceCenterAccessException e) {
				e.printStackTrace();
			}
		}

	}

	private static RAM constructRAM(VMModel model) {
		RAM ram = new RAM();
		ram.setCapacity(model.getMemory());
		return ram;
	}

	private static CPU constructCPU(VMModel model) {
		List<CPUCore> cores = new ArrayList<CPUCore>();
		CPU cpu = new CPU();
		for (int core = 0; core < model.getCores(); core++) {
			CPUCore cpuCore = new CPUCore();
			cpuCore.setFrequency(model.getCpu());
			cpuCore.setCpu(cpu);
			cpuCore.setWeight(1);
			cores.add(cpuCore);			
		}		
		cpu.setCores(cores);
		return cpu;
	}

}
