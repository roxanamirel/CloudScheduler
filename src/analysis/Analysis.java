package analysis;

import planning.ReinforcementLearning;
import reasoning.Evaluator;
import database.model.DataCenter;


/** */
public class Analysis
{
	/** */
	private DataCenter dataCenter;
	private static final float THRESHOLD = 0;
	/** */
	private DataCenter recreateModel()
	{
		return dataCenter;
	
	}
	
	/** */
	private float evaluateRules()
	{
		Evaluator evaluator = new Evaluator(dataCenter);
		return evaluator.computeEntropy(dataCenter);		
	
	}
	
	/** */
	private void decidePlanning(float entropy)
	{
		if(entropy > THRESHOLD) 
				new ReinforcementLearning(dataCenter);
	}
	
	public void startAnalysis()
	{
		dataCenter = recreateModel();
		float entropy = evaluateRules();
		decidePlanning(entropy);
		
		
	}
}
