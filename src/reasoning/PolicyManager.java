package reasoning;

import java.util.ArrayList;
import java.util.List;

import database.model.Resource;
import database.model.Server;
import database.model.VirtualMachine;

/**
 * @author oneadmin
 * 
 */
public class PolicyManager {

	private List<PolicyInstance> resourcePolicies;
	
	public PolicyManager() {
		this.resourcePolicies = new ArrayList<PolicyInstance>();
	}

	/**
	 * se itereaza resurse: res
	 * se itereaza policiti: pol
	 * daca sunt compatibile => add new PolicyResource(res, pol)
	 * compatibilitate dupa clase => instanceof
	 * @param resources
	 *            = list of resources
	 * @param policies
	 *            = list of policies
	 * @return resourcePolicies
	 */
	public List<PolicyInstance> instantiatePolicies(List<Resource> resources,
			List<Policy> policies) {

		for (Resource resource : resources) {
			for (Policy policy : policies) {
				if ((resource instanceof VirtualMachine && policy instanceof VMPolicy)
						|| (resource instanceof Server && policy instanceof ServerPolicy)) {
					PolicyInstance policyInstance = new PolicyInstance(resource, policy);
					resourcePolicies.add(policyInstance);
				}
			}
		}
		return resourcePolicies;

	}

	/**
	 * Evaluates all policies, so we can next determine if they are violated or
	 * not
	 */
	public void evaluatePolicies() {
		for (PolicyInstance policyInstance : resourcePolicies) {
			policyInstance.evaluate();
		}
	}

	/**
	 * Retrieves the list of violated policies
	 * 
	 * @return violated policies
	 */
	public List<PolicyInstance> getViolatedPolicies() {
		List<PolicyInstance> violatedPolicies = new ArrayList<PolicyInstance>();
		for (PolicyInstance policy : resourcePolicies) {
			if (policy.isViolated()) {
				violatedPolicies.add(policy);
			}
		}
		return violatedPolicies;
	}

	/**
	 * @return the resourcePolicies
	 */
	public List<PolicyInstance> getResourcePolicies() {
		return resourcePolicies;
	}
}
