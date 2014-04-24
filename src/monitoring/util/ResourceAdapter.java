package monitoring.util;

import java.util.ArrayList;
import java.util.List;

import models.VMModel;
import services.VMService;
import database.facade.ServerFacade;
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
				// virtualMachine.setHost(constructHost(model));
				// constructHost(model);
			} catch (ServiceCenterAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private static void constructHost(VMModel model) {
		Server host1 = new Server();
		Server host2 = new Server();
		host1.setID(52);
		host2.setID(55);
		RAM ram = new RAM();
		ram.setCapacity(3.7f);
		host1.setRAM(ram);
		List<CPUCore> cores = new ArrayList<CPUCore>();

		for (int i = 0; i < 4; i++) {
			CPUCore core = new CPUCore();
			core.setFrequency(3.07f);
			cores.add(core);
		}
		CPU cpu = new CPU();
		cpu.setCPU(cores);
		host1.setCPU(cpu);
		List<CPUCore> corees = new ArrayList<CPUCore>();
		for (int i = 0; i < 8; i++) {
			CPUCore core = new CPUCore();
			core.setFrequency(2.93f);
			corees.add(core);
		}

		CPU cpuu = new CPU();
		cpu.setCPU(corees);
		host2.setCPU(cpuu);
		RAM ramm = new RAM();
		ramm.setCapacity(5.8f);
		host2.setRAM(ramm);
		FacadeFactory facadeFactory = new FacadeFactory();
		ServerFacade serverFacade = facadeFactory.createServerFacade();
		serverFacade.save(host1);
		serverFacade.save(host2);

	}

	private static RAM constructRAM(VMModel model) {
		RAM ram = new RAM();
		ram.setCapacity(model.getMemory());
		return ram;
	}

	private static CPU constructCPU(VMModel model) {
		List<CPUCore> cores = new ArrayList<CPUCore>();
		for (int core = 0; core < model.getCores(); core++) {
			CPUCore cpuCore = new CPUCore();
			cpuCore.setFrequency(model.getCpu());
			cores.add(cpuCore);
		}
		CPU cpu = new CPU();
		cpu.setCPU(cores);
		return cpu;
	}

}
