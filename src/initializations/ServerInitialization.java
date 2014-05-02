package initializations;

import java.util.ArrayList;
import java.util.List;
import monitoring.util.FacadeFactory;
import database.facade.ServerFacade;
import database.model.CPU;
import database.model.CPUCore;
import database.model.DataCenter;
import database.model.RAM;
import database.model.Server;

public class ServerInitialization {
	
	public static void main(String[] args) {
		constructHost();
	}
	
	private static void constructHost() {
		Server host1 = new Server();
		Server host2 = new Server();
		host1.setID(52);
		host2.setID(55);
		RAM ram = new RAM();
		ram.setCapacity(3.7f);
		host1.setRAM(ram);
		List<CPUCore> cores = new ArrayList<CPUCore>();
		CPU cpu = new CPU();
		for (int i = 0; i < 4; i++) {
			CPUCore core = new CPUCore();
			core.setFrequency(3.07f);
			core.setWeight(1);
			core.setCpu(cpu);
			cores.add(core);
		}
		cpu.setCores(cores);
		host1.setCPU(cpu);

		CPU cpuu = new CPU();
		List<CPUCore> corees = new ArrayList<CPUCore>();
		for (int i = 0; i < 8; i++) {
			CPUCore core = new CPUCore();
			core.setFrequency(2.93f);
			core.setCpu(cpuu);
			core.setWeight(1);
			corees.add(core);
		}
		cpuu.setCores(corees);
		host2.setCPU(cpuu);
		RAM ramm = new RAM();
		ramm.setCapacity(5.8f);
		host2.setRAM(ramm);
		FacadeFactory facadeFactory = new FacadeFactory();
		ServerFacade serverFacade = facadeFactory.createServerFacade();
		DataCenter dataCenter = new DataCenter();
		dataCenter.setID(1);
		facadeFactory.createDataCenterFacade().save(dataCenter);
		host1.setState("ON");
		host2.setState("ON");
		host1.setDataCenter(dataCenter);
		host2.setDataCenter(dataCenter);
		serverFacade.save(host1);
		serverFacade.save(host2);

	}
}
