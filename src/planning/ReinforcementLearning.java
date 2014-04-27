package planning;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import os.ServerOperations;

import logger.CloudLogger;
import models.ServerModel;
import monitoring.util.FacadeFactory;

import reasoning.Evaluator;
import reasoning.PolicyInstance;
import services.ServerService;
import database.model.DataCenter;
import database.model.Resource;
import database.model.Server;
import database.model.VirtualMachine;
import enums.ServiceType;
import exceptions.ServiceCenterAccessException;
import factory.CloudManagerFactory;

public class ReinforcementLearning {

	private DataCenter dataCenter;
	private PriorityQueue<Node> pQueue;
	private float THRESHOLD_ENTROPY = 0;

	public ReinforcementLearning(DataCenter dataCenter, float entropy) {
		this.dataCenter = dataCenter;
		this.pQueue = constructPriorityQueue(entropy);
		this.reinforcementLearning(pQueue, null);
	}

	/** */
	public List<Action> reinforcementLearning(PriorityQueue<Node> pQueue,
			Node highestRewardNode) {
		Node currentNode = pQueue.poll();
		if (currentNode == null) {
			return highestRewardNode.getActionSequence();
		}
		if (currentNode.getEntropy() < THRESHOLD_ENTROPY) {
			return currentNode.getActionSequence();
		}
		if ((highestRewardNode == null)
				|| (currentNode.getReward() > highestRewardNode.getReward())) {
			highestRewardNode = currentNode;
		//} else {
			// actual algorithm
			// 1. compute data center for current node
			SimulateAction simulation = new SimulateAction(dataCenter);
			dataCenter = simulation.executeActions(currentNode
					.getActionSequence());
			
			// 2. get broken policies of the current node
			Evaluator evaluator = new Evaluator(dataCenter);
			List<PolicyInstance> broken_GPI_KPI_Policies = evaluator
					.getViolatedPolicies();
			// 3. undo actions on data center
						dataCenter = simulation
								.undoActions(currentNode.getActionSequence());
			Node nextNode = null;
			// 4. iterate through policies
			for (PolicyInstance policy : broken_GPI_KPI_Policies) {
				Resource subject = policy.getResource();
				if (subject instanceof VirtualMachine) {
					VirtualMachine vm = (VirtualMachine) subject;
					Server deploymentServer = findBestMatchingServer(
							dataCenter, vm);
					if (deploymentServer != null) {
						nextNode = generateLeaf(currentNode, "DEPLOY", null,
								deploymentServer, vm);
					} else {
						// need to wake-up one server
						deploymentServer = findServerToWakeUp(dataCenter);
						if (deploymentServer != null) {
							nextNode = generateLeaf(currentNode, "WAKEUP",
									deploymentServer, null, null);
						} else {
							// data center cannot fit any more VM's => need
							// INTER CLOUD MIGRATION!!!
						}
					}
				}
				if (subject instanceof Server) {
					Server server = (Server) subject;
					for (VirtualMachine localVM : server.getRunningVMs()) {
						Server deploymentServer = findBestMatchingServer(
								dataCenter, localVM);
						if (deploymentServer != null) {
							nextNode = generateLeaf(currentNode, "MIGRATE",
									server, deploymentServer, localVM);
						}
					}
					if (server.getRunningVMs().size() == 0) {
						// shut down server
						nextNode = generateLeaf(currentNode, "SHUTDOWN",
								server, null, null);
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

	/**
	 * iterate through all servers from the data center
	 * find server that is best filled by the VM in a greedy manner
	 * BEST FIT
	 * @param dataCenter
	 * @param vm
	 * @return server that is best filled by the VM
	 */
	private Server findBestMatchingServer(DataCenter dataCenter,
			VirtualMachine vm) {
		// TODO instead of findAll(), eventual findByDataCenter
		FacadeFactory factory = new FacadeFactory();
		List<Server> allServers = factory.createServerFacade().findAll();
		
		float bestRemainingRAMCapacity = Float.MAX_VALUE;
		float bestRemainingCPUFrequency = Float.MAX_VALUE;
		Server bestServer = null;
		
		for(Server server: allServers){
			float availableRAMCapacity = server.getAvailableRAMCapacity() - vm.getRAM().getCapacity();
			float availableCPUFrequency = server.getAvailableCPUFrequency() - vm.getCPU().getTotalFrequency();
			if (availableRAMCapacity  < bestRemainingRAMCapacity &&
					availableCPUFrequency < bestRemainingCPUFrequency) {
				bestRemainingRAMCapacity = availableRAMCapacity;
				bestRemainingCPUFrequency = availableCPUFrequency;
				bestServer = server;
			}				
		}
		return bestServer;
	}

	/** */
	public Node generateLeaf(Node currentNode, String actionType, Server src,
			Server dest, VirtualMachine vm) {
		Action action = null;
		float entropy = 0;
		float oldEntropy = currentNode.getEntropy();
		float oldReward = currentNode.getReward();
		Node newNode = new Node();
		switch (actionType) {
		case "DEPLOY":
			action = new Deploy(src, dest, vm);
			break;
		case "MIGRATE":

			break;
		case "WAKEUP":

			break;
		case "SHUTDOWN":
			ServerService serverService = (ServerService) CloudManagerFactory.getService(ServiceType.SERVER);
			ServerModel serverModel = null;
			try {
				serverModel = serverService.getById(src.getID());
			} catch (ServiceCenterAccessException e) {
				CloudLogger.getInstance().LogInfo(e.getMessage());
				e.printStackTrace();
			}
			ServerOperations.shutDown(serverModel);
			break;
		}
		// 1. compute data center for current node
		SimulateAction simulation = new SimulateAction(dataCenter);
		List<Action> actions = currentNode.getActionSequence();
		// CHECK TO BE ADDED AT THE END
		actions.add(action);
		dataCenter = simulation.executeActions(actions);
		// 2. get broken policies of the current node
		Evaluator evaluator = new Evaluator(dataCenter);
		entropy = evaluator.computeEntropy(dataCenter);
		// 3. undo actions on data center
		dataCenter = simulation.undoActions(actions);
		// 4. instantiate node
		newNode.setActionSequence(actions);
		newNode.setEntropy(entropy);
		// PAGINA 38 DELIVARABLE GAMES
		float reward = newNode.computeReward(oldReward, entropy, oldEntropy,
				action.getCost());
		newNode.setReward(reward);

		return newNode;

	}

	private PriorityQueue<Node> constructPriorityQueue(float entropy) {
		Node root = new Node();
		root.setEntropy(entropy);
		 Evaluator evaluator = new Evaluator(dataCenter);
		 List<PolicyInstance> broken_GPI_KPI_Policies = evaluator.getViolatedPolicies();
		 List<Action> actionSequence = new ArrayList<Action>();
		 for(PolicyInstance policy: broken_GPI_KPI_Policies){
			Resource subject = policy.getResource();
			if(subject instanceof VirtualMachine){
				VirtualMachine vm = (VirtualMachine)subject;
				Server deploymentServer = findBestMatchingServer(dataCenter, vm);
				if(deploymentServer!=null){
					Action action  = new Deploy(null, deploymentServer,vm);
					actionSequence.add(action);
					root.setActionSequence(actionSequence);
					root.setReward(root.computeReward(3, entropy, 0, 1));
					break;
				}
			}
		}
		
		 PriorityQueue<Node> queue = new PriorityQueue<Node>();
		 queue.add(root);
		return queue;
	}
}
