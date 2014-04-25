package reference.model;

import java.util.ArrayList;
import java.util.List;

import reasoning.Policy;
import reasoning.ServerPolicy;

import monitoring.types.Type;
import database.model.CPU;
import database.model.CPUCore;
import database.model.HDD;
import database.model.RAM;
import database.model.Resource;
import database.model.Server;
import database.model.VirtualMachine;

public class ModelReferenceFactory {

	public static Resource getReference(Type type) {
		switch (type) {
		case VM:
			// TODO create default values for vm's
			return new VirtualMachine(createServerCPUReference(),
					createServerRAMReference(), createServerHDDReference());
		case SERVER:
			return new Server(createServerCPUReference(),
					createServerRAMReference(), createServerHDDReference());
		default:
			return null;

		}

	}

	public static Server createServerReference(Policy policy) {
		ServerPolicy serverPolicy = (ServerPolicy) policy;

		List<CPUCore> cores = new ArrayList<CPUCore>();
		cores.add(new CPUCore(serverPolicy.getCpuMax(), serverPolicy
				.getWeight()));

		return new Server(
				new CPU(cores), 
				new RAM(serverPolicy.getRamMax()),
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
