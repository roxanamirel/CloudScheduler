package reasoning;

import java.util.List;

import database.model.Resource;

/** */
public abstract class Policy {

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

}
