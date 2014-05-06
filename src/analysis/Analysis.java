package analysis;

import initializations.PolicyPool;

import logger.CloudLogger;
import monitoring.util.FacadeFactory;
import planning.ReinforcementLearning;
import reasoning.Evaluator;
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
		dataCenter.setPolicyPool(PolicyPool.getPolicyPool());
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
		if (entropy > THRESHOLD) {
			CloudLogger.getInstance().LogInfo(
					"Starting Reinforcement Learning...");
			new ReinforcementLearning(dataCenter, entropy);
		}
	}

}
