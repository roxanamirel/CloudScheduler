package reasoning;

import java.util.List;
import database.model.Resource;
import database.model.Server;

public class ServerPolicy extends Policy {

	private float cpuMin;
	private float cpuMax;
	private float ramMin;
	private float ramMax;

	public ServerPolicy(RuleName name, float weight, List<String> params) {
		super(name, weight, params);
		initializeParams(params);
	}

	private void initializeParams(List<String> params) {
		this.cpuMin = Float.parseFloat(params.get(0));
		this.cpuMax = Float.parseFloat(params.get(1));
		this.ramMin = Float.parseFloat(params.get(2));
		this.ramMin = Float.parseFloat(params.get(3));
	}

	@Override
	public boolean evaluate(Resource r) {
		Server server = (Server) r;
		return server.getCPU().getTotalFrequency() > this.cpuMin && 
			   server.getCPU().getTotalFrequency() < this.cpuMax && 
			   server.getRAM().getCapacity()       > this.cpuMin && 
			   server.getRAM().getCapacity()       < this.cpuMax;

	}

	/**
	 * The violation degree of a low level GPI/KPI policy representing the
	 * server load values is calculated in a similar manner, the only difference
	 * is given by that fact that in this case the server�s optimal computing
	 * resources load values and the server�s actual load values are represented
	 * in the Euclidean space.
	 * */
	@Override
	public float computeQoSViolation(Resource r, Resource reference) {
		Server optimalLoad = (Server) reference;
		Server server = (Server) r;
		double absRam = Math.abs(optimalLoad.getRAM().getCapacity()
				- server.getRAM().getCapacity());
		double absCPU = Math.abs(optimalLoad.getCPU().getTotalFrequency()
				- server.getCPU().getTotalFrequency());
		double absHDD = Math.abs(optimalLoad.getHDD().getCapacity()
				- server.getHDD().getCapacity());
		return (float) Math.sqrt(Math.pow(absRam, 2) + Math.pow(absCPU, 2)
				+ Math.pow(absHDD, 2));
	}

	/**
	 * @return the cpuMin
	 */
	public float getCpuMin() {
		return cpuMin;
	}

	/**
	 * @return the cpuMax
	 */
	public float getCpuMax() {
		return cpuMax;
	}

	/**
	 * @return the ramMin
	 */
	public float getRamMin() {
		return ramMin;
	}

	/**
	 * @return the ramMax
	 */
	public float getRamMax() {
		return ramMax;
	}
}
