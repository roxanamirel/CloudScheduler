package planning;

import java.util.List;

public class Node implements Comparable<Node> {

	private float entropy;
	private float reward;
	private static final float GAMMA = 1;
	private List<Action> actionSequence;

	@Override
	public int compareTo(Node obj) {
		if (this.reward < obj.reward)
			return 1;
		else if (this.reward > obj.reward)
			return -1;
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

	public float computeReward(float reward, float entropy, float oldEntropy,
			float cost) {
		return reward + GAMMA * (oldEntropy - entropy - cost);
	}
}
