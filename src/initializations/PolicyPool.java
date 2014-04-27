package initializations;

import java.util.ArrayList;
import java.util.List;

import reasoning.Policy;
import reasoning.PolicyFactory;
import reasoning.RuleFileMapper;
import reasoning.RuleFileMapperReader;

public class PolicyPool {
	public static List<Policy> getPolicyPool() {
		List<RuleFileMapper> rules = RuleFileMapperReader.readFromFile();
		List<Policy> policyPool = new ArrayList<Policy>();
		for (RuleFileMapper rule : rules) {
			policyPool.add(PolicyFactory.createPolicy(rule));
		}
		return policyPool;
	}
}
