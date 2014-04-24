package reasoning;

import java.util.List;

import database.model.DataCenter;

/** */
public class Evaluator {

	private DataCenter dataCenter;
	private PolicyManager policyManager;

	public Evaluator(DataCenter dataCenter) {
		this.dataCenter = dataCenter;
		policyManager = new PolicyManager();
		policyManager.instantiatePolicies(dataCenter.getAllResources(),
				dataCenter.getPolicyPool());
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
	public float computeEntropy(DataCenter dataCenter) {
		policyManager.evaluatePolicies();
		/*
		 * AICI MAI TREBUIE VAZUT EXACT CUM SE CALCULEAZA!!!
		 * 
		 * foreach violated policyInstance { if policyInstance.policy instanceOf
		 * VMPolicy entropy += policyInstance.policy.score *
		 * policyInstance.policy.computeQoSViolation(VM, Server_CU_VALORILE_0);
		 * //in cazul acesta masina virtuala nu e asignata la niciun server,
		 * deci //ponderea e foarte mare
		 * 
		 * if policyInstance.policy instanceOf ServerPolicy entropy +=
		 * policyInstance.policy.score *
		 * policyInstance.policy.computeQoSViolation(Server, Server_CU_VALORILE
		 * DIN POLITICA); }
		 */
		return 0;

	}

	public List<PolicyInstance> getViolatedPolicies() {
		policyManager.evaluatePolicies();
		return policyManager.getViolatedPolicies();
	}

}
