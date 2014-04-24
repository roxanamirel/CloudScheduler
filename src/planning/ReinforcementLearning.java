package planning;

import java.util.List;
import java.util.PriorityQueue;

import reasoning.Evaluator;
import reasoning.PolicyInstance;
import database.model.DataCenter;
import database.model.Resource;
import database.model.Server;
import database.model.VirtualMachine;

public class ReinforcementLearning{
	
	private DataCenter dataCenter;	
	private PriorityQueue<Node> pQueue;	
	private float THRESHOLD_ENTROPY  = 0;
	
	public ReinforcementLearning(DataCenter dataCenter2) {
		// TODO Auto-generated constructor stub
	}

	/** */
	public List<Action> reinforcementLearning(PriorityQueue<Node> pQueue, Node highestRewardNode)
	{
	 Node currentNode = pQueue.poll();
	 if(currentNode == null) {
		 return highestRewardNode.getActionSequence();
	 }
	 if(currentNode.getEntropy() < THRESHOLD_ENTROPY){
		 return currentNode.getActionSequence();
	 }
	 if((highestRewardNode == null)||(currentNode.getReward() > highestRewardNode.getReward())){
		 highestRewardNode = currentNode;
	 }else{
		 //actual algorithm
		 //1. compute data center for current node
		 SimulateAction simulation = new SimulateAction(dataCenter);
		 dataCenter = simulation.executeActions(currentNode.getActionSequence());
		 //2. get broken policies of the current node
		 Evaluator evaluator = new Evaluator(dataCenter);
		 List<PolicyInstance> broken_GPI_KPI_Policies = evaluator.getViolatedPolicies();
		 //3. undo actions on data center
		 dataCenter = simulation.undoActions(currentNode.getActionSequence());
		 Node nextNode = null;
		 //4. iterate through policies
		 for(PolicyInstance policy: broken_GPI_KPI_Policies){
			Resource subject = policy.resource;
			if(subject instanceof VirtualMachine){
				VirtualMachine vm = (VirtualMachine)subject;
				Server deploymentServer = findBestMatchingServer(dataCenter, vm);
				if(deploymentServer!=null){
					nextNode = generateLeaf(currentNode, "DEPLOYT", null, deploymentServer, vm);
				}else{
					//need to wake-up one server
					deploymentServer = findServerToWakeUp(dataCenter);
					if(deploymentServer!= null){
						nextNode = generateLeaf(currentNode, "WAKEUP", deploymentServer, null, null);
					}
					else{
						//data center cannot fit any more VM's => need INTER CLOUD MIGRATION!!!
					}
				}
			}
			if(subject instanceof Server){
				Server server = (Server) subject;
				for(VirtualMachine localVM : server.getRunningVMs()){
					Server deploymentServer = findBestMatchingServer(dataCenter, localVM);
					if(deploymentServer != null){
						nextNode = generateLeaf(currentNode, "MIGRATE", server, deploymentServer, localVM);
					}
				}
				if(server.getRunningVMs().size()==0){
					//shut down server
					nextNode = generateLeaf(currentNode, "SHUTDOWN", server, null, null);
				}
			}
			pQueue.add(nextNode);
		 }
	 }
	 return reinforcementLearning(pQueue, highestRewardNode);
	}
	
	private Server findServerToWakeUp(DataCenter dataCenter2) {
		// TODO Auto-generated method stub
		return null;
	}

	private Server findBestMatchingServer(DataCenter dataCenter2, VirtualMachine vm) {
		// TODO Auto-generated method stub
		//iterate through all servers from the data center
		//find server that is best filled by the VM in a greedy manner
		//BEST FIT
		
		return null;
	}

	/** */
	public Node generateLeaf(Node currentNode, String actionType,  Server src, Server dest,  VirtualMachine vm)
	{
		Action action = null;
		float entropy = 0;
		float oldEntropy = currentNode.getEntropy();
		float oldReward = currentNode.getReward();
		Node newNode = new Node();
		switch (actionType){
		case "DEPLOY":
			action = new Deploy(src,dest,vm);
			break;
		case "MIGRATE":
			
			break;
		case "WAKEUP":
			
			break;
		case "SHUTDOWN":
			
			break;
		}
		 //1. compute data center for current node
		 SimulateAction simulation = new SimulateAction(dataCenter);
		 List<Action> actions = currentNode.getActionSequence();
		 //CHECK TO BE ADDED AT THE END
		 actions.add(action);
		 dataCenter = simulation.executeActions(actions);
		 //2. get broken policies of the current node
		 Evaluator evaluator = new Evaluator(dataCenter);
		 entropy = evaluator.computeEntropy(dataCenter);
		 //3. undo actions on data center
		 dataCenter = simulation.undoActions(actions);
		 //4. instantiate node
		 newNode.setActionSequence(actions);
		 newNode.setEntropy(entropy);
		 //PAGINA 38 DELIVARABLE GAMES
		 float reward = newNode.computeReward(oldReward, entropy, oldEntropy, action.getCost());
		 newNode.setReward(reward);
		
		return newNode;
	
	}
}
