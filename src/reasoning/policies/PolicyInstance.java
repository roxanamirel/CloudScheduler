package reasoning.policies;

import database.model.Resource;

/**
 * @author oneadmin
 *
 */
public class PolicyInstance
{
	private boolean isViolated;	
	private Resource resource;	
	private Policy policy;
	
	public PolicyInstance(Resource resource, Policy policy) {
		this.resource = resource;
		this.policy = policy;
	}

	public void evaluate() {
		isViolated = policy.evaluate(resource);
	}
	
	/**
	 * @return the isViolated
	 */
	public boolean isViolated() {
		return isViolated;
	}

	/**
	 * @param isViolated the isViolated to set
	 */
	public void setViolated(boolean isViolated) {
		this.isViolated = isViolated;
	}

	/**
	 * @return the resource
	 */
	public Resource getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	/**
	 * @return the policy
	 */
	public Policy getPolicy() {
		return policy;
	}

	/**
	 * @param policy the policy to set
	 */
	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
}
