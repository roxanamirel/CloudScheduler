package reasoning;

import java.util.List;

public class RuleFileMapper {
	private RuleName name;
	private float weight;
	private List<String> params;

	/**
	 * @return the name
	 */
	public RuleName getName() {
		return name;
	}

	/**
	 * @return the weight
	 */
	public float getWeight() {
		return weight;
	}

	/**
	 * @return the params
	 */
	public List<String> getParams() {
		return params;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(RuleName name) {
		this.name = name;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(List<String> params) {
		this.params = params;
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RuleMapper [name=" + name + ", weight=" + weight + ", params="
				+ params + "]";
	}

}
