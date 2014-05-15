package reasoning.policies;

import java.io.Serializable;
import java.util.List;

import reasoning.RuleName;
import database.model.Resource;

/** */
public abstract class Policy implements Serializable {

	private RuleName name;
	/**
	 * Score associated with the violation of the policy
	 * */
	private float weight;

	private List<String> params;

	/**
	 * Parameters passed from Abstract factory
	 * */
	public Policy(RuleName name, float weight, List<String> params) {
		this.name = name;
		this.weight = weight;
		this.params = params;

	}

	public abstract boolean evaluate(Resource r);

	public abstract float computeQoSViolation(Resource r, Resource reference);

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weigth) {
		this.weight = weigth;
	}

	/**
	 * @return the name
	 */
	public RuleName getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(RuleName name) {
		this.name = name;
	}

	/**
	 * @return the params
	 */
	public List<String> getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(List<String> params) {
		this.params = params;
	}

}
