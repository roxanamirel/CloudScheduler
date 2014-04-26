package analysis;

import java.util.ArrayList;
import java.util.List;

import monitoring.util.FacadeFactory;
import planning.ReinforcementLearning;
import reasoning.Evaluator;
import reasoning.Policy;
import reasoning.PolicyFactory;
import reasoning.RuleFileMapper;
import reasoning.RuleFileMapperReader;
import database.connection.DBConnection;
import database.dao.DataCenterDAO;
import database.facade.DataCenterFacade;
import database.facade.DataCenterFacadeImpl;
import database.model.DataCenter;

/**
 * @author oneadmin
 * 
 */
public class Analysis {
	private DataCenter dataCenter;
	private static final float THRESHOLD = 0;

	public void startAnalysis() {
		dataCenter = recreateModel();
		dataCenter.setPolicyPool(getPolicyPool());
		float entropy = evaluateRules();
		decidePlanning(entropy);
	}

	private DataCenter recreateModel() {
		FacadeFactory facadeFactory = new FacadeFactory();
		return facadeFactory.createDataCenterFacade().findAll().get(0);
	}

	/** */
	private float evaluateRules() {
		Evaluator evaluator = new Evaluator(dataCenter);
		return evaluator.computeEntropy(dataCenter);

	}

	/** */
	private void decidePlanning(float entropy) {
		if (entropy > THRESHOLD)
			new ReinforcementLearning(dataCenter,entropy);
	}

	private List<Policy> getPolicyPool() {
		List<RuleFileMapper> rules = RuleFileMapperReader.readFromFile();
		List<Policy> policyPool = new ArrayList<Policy>();
		for (RuleFileMapper rule : rules) {
			policyPool.add(PolicyFactory.createPolicy(rule));
		}
		return policyPool;
	}
}
