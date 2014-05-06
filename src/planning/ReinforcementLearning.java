package planning;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import logger.CloudLogger;
import reasoning.Evaluator;
import reasoning.PolicyInstance;
import util.ServerState;
import database.model.DataCenter;
import database.model.Resource;
import database.model.Server;
import database.model.VirtualMachine;
import execution.Execution;

public class ReinforcementLearning {

	private DataCenter dataCenter;
	private PriorityQueue<Node> pQueue;
	private float THRESHOLD_ENTROPY = 0;

	public ReinforcementLearning(DataCenter dataCenter, float entropy) {
		this.dataCenter = dataCenter;
		this.pQueue = constructPriorityQueue(entropy);
		List<Action> finalActions = this.reinforcementLearning(pQueue, null);
		executeFinalActions(finalActions);
	}

	private PriorityQueue<Node> constructPriorityQueue(float oldEntropy) {
		Node root = new Node();
		// root.setEntropy(oldEntropy);
		Evaluator evaluator = new Evaluator(dataCenter);
		List<PolicyInstance> broken_GPI_KPI_Policies = evaluator
				.getViolatedPolicies();
		List<Action> actionSequence = new ArrayList<Action>();
		for (PolicyInstance policy : broken_GPI_KPI_Policies) {
			Resource subject = policy.getResource();
			if (subject instanceof VirtualMachine) {
				VirtualMachine vm = (VirtualMachine) subject;
				Server deploymentServer = findBestMatchingServer(dataCenter, vm);
				if (deploymentServer != null) {

					Action action = new Deploy(null, deploymentServer, vm);

					// /////
					actionSequence.add(action);
					SimulateAction simulation = new SimulateAction(dataCenter);
					dataCenter = simulation.doActions(actionSequence);
					// 2. get broken policies of the current node
					float entropy = evaluator.computeEntropy(dataCenter);
					// 3. undo actions on data center
					dataCenter = simulation.undoActions(actionSequence);

					// //////
					// actionSequence.add(action);
					root.setEntropy(entropy);
					root.setActionSequence(actionSequence);
					root.setReward(root.computeReward(0, root.getEntropy(),
							oldEntropy, action.getCost()));
					break;
				}
			}
		}

		PriorityQueue<Node> queue = new PriorityQueue<Node>();
		queue.add(root);
		return queue;
	}

	/**
	 * Returns the actions to be executed after applying the algorithm
	 * 
	 * @param pQueue
	 *            = priority queue storing the nodes
	 * @param highestRewardNode
	 *            = node with the best reward
	 * @return list of actions after applying the algorithm
	 */
	private List<Action> reinforcementLearning(PriorityQueue<Node> pQueue,
			Node highestRewardNode) {
		Node currentNode = pQueue.poll();
		if (currentNode == null) {
			return highestRewardNode.getActionSequence();
		}
		if (currentNode.getEntropy() <= THRESHOLD_ENTROPY) {
			return currentNode.getActionSequence();
		}
		if ((highestRewardNode == null)
				|| (currentNode.getReward() > highestRewardNode.getReward())) {
			highestRewardNode = currentNode;

			// } else {
			// actual algorithm

			// 1. compute data center for current node
			SimulateAction simulation = new SimulateAction(dataCenter);
			dataCenter = simulation.doActions(currentNode.getActionSequence());
			// 2. get broken policies of the current node
			Evaluator evaluator = new Evaluator(dataCenter);
			List<PolicyInstance> broken_GPI_KPI_Policies = evaluator
					.getViolatedPolicies();

			// 3. undo actions on data center
			Node nextNode = null;

			// 4. iterate through policies
			Iterator<PolicyInstance> iterator = broken_GPI_KPI_Policies
					.iterator();
			while (iterator.hasNext()) {
				PolicyInstance policy = iterator.next();
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
							System.out.println("intercloud mig");
							// data center cannot fit any more VM's => need
							// INTER CLOUD MIGRATION!!!
						}
					}
				}
				if (subject instanceof Server) {
					Server server = (Server) subject;
					if (server.getRunningVMs().size() == 0) {
						// shut down server
						nextNode = generateLeaf(currentNode, "SHUTDOWN",
								server, null, null);
					}

					List<VirtualMachine> copy = new ArrayList<VirtualMachine>(
							server.getRunningVMs());
					for (VirtualMachine localVM : copy) {
						Server deploymentServer = findBestMatchingServer(
								dataCenter, localVM);
						if (deploymentServer == null) {
							deploymentServer = findServerToWakeUp(dataCenter);
							if (deploymentServer != null) {
								nextNode = generateLeaf(currentNode, "WAKEUP",
										deploymentServer, null, null);
							}
						} else {
							nextNode = generateLeaf(currentNode, "MIGRATE",
									server, deploymentServer, localVM);
						}
					}
				}
				pQueue.add(nextNode);
			}
			dataCenter = simulation
					.undoActions(currentNode.getActionSequence());
		}

		return reinforcementLearning(pQueue, highestRewardNode);
	}

	private void executeFinalActions(List<Action> finalActions) {
		for (Action action : finalActions) {
			CloudLogger.getInstance().LogInfo(
					action.toString() + " will be executed ...");
		}

		Execution execution = new Execution();
		execution.updateDatabase(dataCenter, finalActions);
		execution.executeActions(finalActions);
	}

	private Server findServerToWakeUp(DataCenter dataCenter) {
		List<Server> allServers = dataCenter.getServerPool();
		for (Server server : allServers) {
			if (server.getState().equals(ServerState.OFF.toString())) {
				server.setState(ServerState.ON.toString());
				return server;
			}
		}
		return null;
	}

	/**
	 * iterate through all servers from the data center find server that is best
	 * filled by the VM in a greedy manner BEST FIT
	 * 
	 * @param dataCenter
	 * @param vm
	 * @return server that is best filled by the VM
	 */
	private Server findBestMatchingServer(DataCenter dataCenter,
			VirtualMachine vm) {
		List<Server> allServers = dataCenter.getServerPool();

		float bestRemainingRAMCapacity = Float.MAX_VALUE;
		float bestRemainingCPUFrequency = Float.MAX_VALUE;
		Server bestServer = null;

		for (Server server : allServers) {
			if (server.getState().equals(ServerState.ON.toString())) {
				float availableRAMCapacity = server.getAvailableRAMCapacity()
						- vm.getRAM().getCapacity();
				float availableCPUFrequency = server.getAvailableCPUFrequency()
						- vm.getCPU().getTotalFrequency();
				if (availableRAMCapacity < bestRemainingRAMCapacity
						&& availableCPUFrequency < bestRemainingCPUFrequency
						&& availableRAMCapacity > 0
						&& availableCPUFrequency > 0) {
					bestRemainingRAMCapacity = availableRAMCapacity;
					bestRemainingCPUFrequency = availableCPUFrequency;
					bestServer = server;
				}
			}
		}
		return bestServer;
	}

	private Node generateLeaf(Node currentNode, String actionType, Server src,
			Server dest, VirtualMachine vm) {
		Action action = null;
		float entropy = 0;

		float oldEntropy = currentNode.getEntropy();
		float oldReward = currentNode.getReward();
		switch (actionType) {
		case "DEPLOY":
			action = new Deploy(src, dest, vm);
			break;
		case "MIGRATE":
			action = new Migrate(src, dest, vm);
			break;
		case "WAKEUP":
			action = new TurnOnServer(src, dest, vm);
			break;
		case "SHUTDOWN":
			action = new TurnOffServer(src, dest, vm);
			break;
		}
		// 1. compute data center for current node
		SimulateAction simulation = new SimulateAction(dataCenter);
		List<Action> actions = new ArrayList<Action>();
		actions.addAll(currentNode.getActionSequence());
		// CHECK TO BE ADDED AT THE END
		actions.add(action);
		dataCenter = simulation.doAction(action);
		// 2. get broken policies of the current node
		Evaluator evaluator = new Evaluator(dataCenter);
		entropy = evaluator.computeEntropy(dataCenter);
		// 3. undo actions on data center
		dataCenter = simulation.undoAction(action);
		// 4. instantiate node
		Node newNode = new Node();
		newNode.setActionSequence(actions);
		newNode.setEntropy(entropy);
		// PAGINA 38 DELIVARABLE GAMES
		float reward = newNode.computeReward(oldReward, entropy, oldEntropy,
				action.getCost());
		newNode.setReward(reward);

		return newNode;

	}

}
