package reasoning.policies;

import java.util.List;

import reasoning.RuleName;

import database.model.Resource;
import database.model.Server;
import database.model.VirtualMachine;

public class VMPolicy extends Policy {

	private String state;

	public VMPolicy(RuleName name, float weight, List<String> params) {
		super(name, weight, params);
		initializeState(params);
	}

	private void initializeState(List<String> params) {
		this.state = params.get(0);//is deployed

	}

	@Override
	public boolean evaluate(Resource r) {
		VirtualMachine vm = (VirtualMachine)r;
		return vm.getHost() == null;
	}

	/***
	 * To calculate the violation degree of a low level GPI/KPI policy defining
	 * the virtual activity QoS requests for computing resources we have
	 * represented both the activity allocated server computing resources and
	 * the activity request for resources in a 3D space having as axis the main
	 * server resources (CPU, MEM, HDD).
	 * */

	@Override
	public float computeQoSViolation(Resource virtualMachine, Resource serverReference) {
		VirtualMachine vm = (VirtualMachine) virtualMachine;
		Server server = (Server) serverReference;
		double absRam = Math.abs(vm.getRAM().getCapacity()
				- server.getRAM().getCapacity());
		double absCPU = Math.abs(vm.getCPU().getTotalFrequency()
				- server.getCPU().getTotalFrequency());
//		double absHDD = Math.abs(vm.getHDD().getCapacity()
//				- server.getHDD().getCapacity());
		return (float) Math.sqrt(Math.pow(absRam, 2) + Math.pow(absCPU, 2)) + 2000f;
				//+ Math.pow(absHDD, 2));
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

}
