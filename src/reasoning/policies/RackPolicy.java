package reasoning.policies;

import java.util.List;

import reasoning.RuleName;
import util.ServerState;
import database.model.Rack;
import database.model.Resource;
import database.model.Server;

public class RackPolicy extends Policy {

	private float minUtilization;
	private float maxUtilization;
	
	/**
	 * @return the minUtilization
	 */
	public float getMinUtilization() {
		return minUtilization;
	}

	/**
	 * @param minUtilization the minUtilization to set
	 */
	public void setMinUtilization(float minUtilization) {
		this.minUtilization = minUtilization;
	}

	/**
	 * @return the maxUtilization
	 */
	public float getMaxUtilization() {
		return maxUtilization;
	}

	/**
	 * @param maxUtilization the maxUtilization to set
	 */
	public void setMaxUtilization(float maxUtilization) {
		this.maxUtilization = maxUtilization;
	}

	public RackPolicy(RuleName name, float weight, List<String> params) {
		super(name, weight, params);
		initializeParams(params);
	}

	private void initializeParams(List<String> params) {
		this.minUtilization = Float.parseFloat(params.get(0));
		this.maxUtilization = Float.parseFloat(params.get(1));
	}

	@Override
	public boolean evaluate(Resource r) {
		Rack rack = (Rack) r;
		int totalON = 0;
		for (Server server : rack.getServers()) {
			if (ServerState.valueOf(server.getState()) == ServerState.ON) {
				totalON++;
			}
		}		
		float utilization = (float) totalON / rack.getServers().size() * 100;
		
		return utilization > minUtilization && utilization < maxUtilization;
	}

	@Override
	public float computeQoSViolation(Resource r, Resource reference) {
		Rack rack = (Rack) r;
		Rack optimalRack = (Rack) reference;
		float absCapacity = Math.abs(optimalRack.getCapacity() - rack.getCapacity());
		return (float) absCapacity;
	}

}
