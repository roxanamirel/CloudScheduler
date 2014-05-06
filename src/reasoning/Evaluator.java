package reasoning;

import java.util.List;
import monitoring.types.Type;
import reference.model.ModelReferenceFactory;
import database.model.DataCenter;
import database.model.Server;

public class Evaluator {

	private DataCenter dataCenter;
	private PolicyManager policyManager;

	public Evaluator(DataCenter dataCenter) {
		this.dataCenter = dataCenter;
		policyManager = new PolicyManager();
		policyManager.instantiatePolicies(dataCenter.getAllResources(),
				dataCenter.getPolicyPool());
	}
	
	public List<PolicyInstance> getViolatedPolicies() {
		policyManager.evaluatePolicies();
		return policyManager.getViolatedPolicies();
	}
	
	/**
	 * ). The entropy value of a service center situation (S) is calculated by
	 * setting a predefined threshold for each GPIs/KPIs related policy, and by
	 * evaluating whether the threshold is satisfied or not
	 * 
	 * E = sum (wn * fn)
	 * 
	 * (i) fn is a function defined as fn : pn -> {0,1} in which pn is a policy
	 * describing a GPI/KPI that evaluates the satisfaction of the thresholds
	 * for each GPIs/KPIs and (ii) wn represents the weight of a GPI/KPI policy
	 * showing its importance for the service centre.
	 * */

	/*
	 * AICI MAI TREBUIE VAZUT EXACT CUM SE CALCULEAZA!!!
	 * 
	 * foreach violated policyInstance { if policyInstance.policy instanceOf
	 * VMPolicy 
	 * entropy += policyInstance.policy.score *
	 * policyInstance.policy.computeQoSViolation(VM, Server_CU_VALORILE_0); //in
	 * cazul acesta masina virtuala nu e asignata la niciun server, deci
	 * //ponderea e foarte mare
	 * 
	 * if policyInstance.policy instanceOf ServerPolicy entropy +=
	 * policyInstance.policy.score *
	 * policyInstance.policy.computeQoSViolation(Server, Server_CU_VALORILE DIN
	 * POLITICA); }
	 */
	public float computeEntropy(DataCenter dataCenter) {
		//policyManager.evaluatePolicies();

		Server serverReference = createServerReferenceFromPolicy(dataCenter);

		float entropy = 0;

		List<PolicyInstance> violatedPolicies = getViolatedPolicies();
		for (PolicyInstance violatedPolicyInstance : violatedPolicies) {
			float weight = violatedPolicyInstance.getPolicy().getWeight();
			float violation = 0;
			if (violatedPolicyInstance.getPolicy() instanceof VMPolicy) {
				violation = violatedPolicyInstance
						.getPolicy()
						.computeQoSViolation(
								violatedPolicyInstance.getResource(),
								ModelReferenceFactory.getReference(Type.SERVER));

			}
			if (violatedPolicyInstance.getPolicy() instanceof ServerPolicy) {
				violation = violatedPolicyInstance.getPolicy()
						.computeQoSViolation(
								violatedPolicyInstance.getResource(),
								serverReference);
			}
			entropy += weight * violation;
		}
		return entropy;

	}

	private Server createServerReferenceFromPolicy(DataCenter dataCenter) {
		for (Policy policy : dataCenter.getPolicyPool()) {
			if (policy instanceof ServerPolicy) {
				return ModelReferenceFactory
						.createServerReference(policy);
			}
		}
		return new Server();
	}

	public DataCenter getDataCenter() {
		return dataCenter;
	}

	public void setDataCenter(DataCenter dataCenter) {
		this.dataCenter = dataCenter;
	}

	public PolicyManager getPolicyManager() {
		return policyManager;
	}

	public void setPolicyManager(PolicyManager policyManager) {
		this.policyManager = policyManager;
	}
}
