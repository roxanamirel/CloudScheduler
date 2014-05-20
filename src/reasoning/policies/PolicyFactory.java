package reasoning.policies;

import reasoning.RuleFileMapper;

public class PolicyFactory {
	/**
	 * 1. get RuleFileMapper from the rules file 2. instantiate policy
	 * */
	public static Policy createPolicy(RuleFileMapper rule) {

		switch (rule.getName()) {
		case VM_POLICY:
			return new VMPolicy(rule.getName(), rule.getWeight(),
					rule.getParams());

		case SERVER_POLICY:
			return new ServerPolicy(rule.getName(), rule.getWeight(),
					rule.getParams());
		case RACK_POLICY:
			return new RackPolicy(rule.getName(), rule.getWeight(),
					rule.getParams());
		default:
			return null;
		}
	}

}
