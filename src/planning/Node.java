package planning;

import java.util.List;


public class Node implements Comparable<Node>{

	private float entropy;	
	private float reward;	
	private static final float GAMMA = 1;
	
	private List<Action> actionSequence ;
	
	/** */
	public int compareTo(Node obj)
	{
		//implement compare to based on REWARD
		return 0;
	
	}

	public List<Action> getActionSequence() {
		return actionSequence;
	}

	public void setActionSequence(List<Action> actionSequence) {
		this.actionSequence = actionSequence;
	}

	public float getEntropy() {
		return entropy;
	}

	public void setEntropy(float entropy) {
		this.entropy = entropy;
	}

	public float getReward() {
		return reward;
	}

	public void setReward(float reward) {
		this.reward = reward;
	}

	public float computeReward(float reward2, float entropy2, float oldEntropy,
			float cost) {
		// TODO Auto-generated method stub
		float reward = 0;
		reward = reward2 + GAMMA * (entropy2 - oldEntropy - cost);
		return reward;
		
		
	}
}
