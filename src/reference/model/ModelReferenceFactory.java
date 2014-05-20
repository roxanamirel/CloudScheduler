package reference.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import reasoning.policies.Policy;
import reasoning.policies.RackPolicy;
import reasoning.policies.ServerPolicy;

import monitoring.types.ReferenceModelType;
import database.model.CPU;
import database.model.CPUCore;
import database.model.HDD;
import database.model.RAM;
import database.model.Resource;
import database.model.Server;
import database.model.VirtualMachine;
import database.model.Rack;

public class ModelReferenceFactory {

	public static Resource getReference(ReferenceModelType type) {
		switch (type) {
		case VM:
			return new VirtualMachine(createServerCPUReference(),
					createServerRAMReference(), createServerHDDReference());
		case SERVER:
			return new Server(createServerCPUReference(),
					createServerRAMReference(), createServerHDDReference());
		
		
		default:
			return null;

		}
	}

	public static int createRackCapacityReference(Policy policy, Rack rack) {
		int initialCapacity = rack.getCapacity();
		RackPolicy rp = (RackPolicy)policy;
		float percentage = rp.getMaxUtilization();
		int finalCapacity = (int) (initialCapacity * percentage / 100);
		return finalCapacity;
	}

	public static Server createServerReference(Policy policy) {
		ServerPolicy serverPolicy = (ServerPolicy) policy;
		float cpuMax = serverPolicy.getCpuMax();
		float ramMax = serverPolicy.getRamMax();
		
		List<CPUCore> cores = new ArrayList<CPUCore>();
		List<CPUCore> actualCores = createServerCoresReference();
		Iterator<CPUCore> it = actualCores.iterator();
		while(it.hasNext()){
			CPUCore aux = it.next();
			cores.add(new CPUCore(aux.getFrequency()*cpuMax/100,
					aux.getWeight()));
		}
		
		

		return new Server(new CPU(cores), new RAM(createServerRAMReference().getCapacity()*ramMax / 100),
				new HDD());
	}

	private static HDD createServerHDDReference() {
		return new HDD();
	}

	private static CPU createServerCPUReference() {
		List<CPUCore> cores = createServerCoresReference();
		return new CPU(cores);
	}

	private static List<CPUCore> createServerCoresReference() {
		List<CPUCore> cores = new ArrayList<>();
		cores.add(new CPUCore(0, 0));
		cores.add(new CPUCore(0, 0));
		return cores;
	}

	private static RAM createServerRAMReference() {
		return new RAM(0);
	}
}
