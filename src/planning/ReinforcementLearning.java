package planning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import planning.actions.Action;
import planning.actions.Deploy;
import planning.actions.InterCloudMigration;
import planning.actions.Migrate;
import planning.actions.TurnOffRack;
import planning.actions.TurnOffServer;
import planning.actions.TurnOnServer;

import logger.CloudLogger;
import monitoring.command.Command;
import reasoning.Evaluator;
import reasoning.policies.PolicyInstance;
import util.ServerState;
import database.model.DataCenter;
import database.model.Rack;
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
		List<Action> finalActions = new ArrayList<Action>(
				reinforcementLearning(pQueue, null));
		executeActions(finalActions);
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
		CloudLogger.getInstance().LogInfo(
				"-----------------------------------------------------");
		Node currentNode = pQueue.poll();
		if (currentNode == null) {
			return highestRewardNode.getActionSequence();
		}
		if (currentNode.getEntropy() <= THRESHOLD_ENTROPY) {
			return currentNode.getActionSequence();
		}
		if ((highestRewardNode == null)
				|| (currentNode.getReward() > highestRewardNode.getReward())) {

			// 1. compute data center for current node
			SimulateAction simulation = new SimulateAction(dataCenter);
			dataCenter = simulation.doActions(currentNode.getActionSequence());
			// 2. get broken policies of the current node
			Evaluator evaluator = new Evaluator(dataCenter);
			List<PolicyInstance> broken_GPI_KPI_Policies = evaluator
					.getViolatedPolicies();
			// highestRewardNode = containsTurnOff(currentNode)
			// && getPendingVMs(broken_GPI_KPI_Policies) > 0 ? highestRewardNode
			// : currentNode;
			highestRewardNode = currentNode;
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
						nextNode = generateLeaf(currentNode, Command.DEPLOY,
								null, deploymentServer, vm, null);
						pQueue.add(nextNode);
					} else {
						// need to wake-up one server
						deploymentServer = findServerToWakeUp(dataCenter);
						if (deploymentServer != null) {
							nextNode = generateLeaf(currentNode,
									Command.WAKEUP, deploymentServer, null,
									null, null);
							pQueue.add(nextNode);
						} else {
							// data center cannot fit any more VM's => need
							// INTER CLOUD MIGRATION!!!
							nextNode = generateLeaf(currentNode,
									Command.INTERCLOUDMIGRATE, null, null, vm,
									null);
							pQueue.add(nextNode);
						}
					}
				}
				if (subject instanceof Server) {
					Server server = (Server) subject;
					if (server.getRunningVMs().size() == 0) {
						// shut down server
						nextNode = generateLeaf(currentNode, Command.SHUTDOWN,
								server, null, null, null);
						pQueue.add(nextNode);
					} else {

						List<VirtualMachine> copy = new ArrayList<VirtualMachine>(
								server.getRunningVMs());
						// for?
						VirtualMachine vmCopy = copy.get(copy.size() - 1);
						Server deploymentServer = findBestMatchingServer(
								dataCenter, vmCopy);
						if (deploymentServer == null) {
							deploymentServer = findServerToWakeUp(dataCenter);
							if (deploymentServer != null) {
								nextNode = generateLeaf(currentNode,
										Command.WAKEUP, deploymentServer, null,
										null, null);
								pQueue.add(nextNode);
							} else {
								// data center cannot fit any more VM's => need
								// INTER CLOUD MIGRATION!!!

							}
						} else if (deploymentServer.getID() != server.getID()) {
							nextNode = generateLeaf(currentNode,
									Command.MIGRATE, server, deploymentServer,
									vmCopy, null);
							pQueue.add(nextNode);
						}
					}
				}
				if (subject instanceof Rack) {
					Rack rack = (Rack) subject;
					if (rack.getServers().size() == 0) {
						// shut down rack
						nextNode = generateLeaf(currentNode,
								Command.SHUTDOWN_RACK, null, null, null, rack);
						pQueue.add(nextNode);
					} else {
						/*
						 * Find best matching rack If there are servers that
						 * accept VM, migrate some of the VMs there
						 */
						List<Server> rackServers = rack.getServers();
						Iterator<Server> it = rackServers.iterator();
						List<VirtualMachine> toMigrate = new ArrayList<VirtualMachine>();
						// iterate through servers from this rack
						while (it.hasNext()) {
							Server aux = it.next();
							// add all VMs to an array
							toMigrate.addAll(aux.getRunningVMs());
						}
						Iterator<VirtualMachine> itVM = toMigrate.iterator();
						// iterate through the local VMs to be migrated
						while (itVM.hasNext()) {
							VirtualMachine vm = itVM.next();
							Server deploymentServer = findBestMatchingServerInRack(
									dataCenter, rack, vm);
							if (deploymentServer != null) {
								nextNode = generateLeaf(currentNode,
										Command.MIGRATE, vm.getHost(),
										deploymentServer, vm, null);
								pQueue.add(nextNode);
							} else {
								// need to wake-up one server
								deploymentServer = findServerToWakeUpForRack(
										dataCenter, rack);
								if (deploymentServer != null) {
									nextNode = generateLeaf(currentNode,
											Command.WAKEUP, deploymentServer,
											null, null, null);
									pQueue.add(nextNode);
								}
							}
						}
					}
				}
				// pQueue.add(nextNode);
			}
			dataCenter = simulation
					.undoActions(currentNode.getActionSequence());
		}

		return reinforcementLearning(pQueue, highestRewardNode);
	}

	private boolean containsTurnOff(Node currentNode) {
		for (Action action : currentNode.getActionSequence()) {
			if (action instanceof TurnOffServer)
				return true;
		}
		return false;
	}

	private int getPendingVMs(List<PolicyInstance> broken_GPI_KPI_Policies) {
		int count = 0;
		for (PolicyInstance policyInstance : broken_GPI_KPI_Policies) {
			if (policyInstance.getResource() instanceof VirtualMachine) {
				count++;
			}
		}
		return count;
	}

	private void executeActions(List<Action> finalActions) {
		finalActions = combineActions(finalActions);
		for (Action action : finalActions) {
			CloudLogger.getInstance().LogInfo(
					action.toString() + " will be executed ...");
		}

		Execution execution = new Execution();
		execution.updateDatabase(dataCenter, finalActions);
		execution.executeActions(finalActions);
		
		
	}

	/**
	 * Returns an improved list of actions E.g avoids deploying and migrating
	 * the same virtual machine, by just deploying it on the right server
	 * 
	 * @param finalActions
	 *            = the actions to be combined and improved
	 * @return improved <code>finalActions</code>
	 */
	private List<Action> combineActions(List<Action> finalActions) {

		Map<Integer, LinkedList<Action>> hashMap = createHashMapBasedOnActions(finalActions);
		List<Action> solution = new ArrayList<Action>();

		for (Action action : finalActions) {
			if (action.getVM() != null
					&& hashMap.containsKey(action.getVM().getID())
					&& hashMap.get(action.getVM().getID()).size() > 1) {
				LinkedList<Action> actions = hashMap
						.get(action.getVM().getID());

				if (actions.getFirst() instanceof Deploy
						&& actions.getLast() instanceof Migrate) {
					Server deploymentServer = actions.getLast()
							.getDestinationServer();
					VirtualMachine vm = actions.getFirst().getVM();
					Action deploy = new Deploy(null, deploymentServer, vm);
					if (!solution.contains(deploy)) {
						solution.add(deploy);
					}
				}
			} else {
				solution.add(action);
			}
		}

		return solution;
	}

	/**
	 * Creates a map containing as key virtual machine's id, and as values a
	 * linkedList containing the operations to be performed on that virtual
	 * machine
	 * 
	 * @param actions
	 *            are used to build the map
	 * @return a map containing the all the actions associated to each virtual
	 *         machine's id
	 */
	private Map<Integer, LinkedList<Action>> createHashMapBasedOnActions(
			List<Action> actions) {
		Map<Integer, LinkedList<Action>> hashMap = new HashMap<Integer, LinkedList<Action>>();
		for (Action action : actions) {
			if (action instanceof Deploy || action instanceof Migrate) {
				if (hashMap.get(action.getVM().getID()) == null) {
					hashMap.put(action.getVM().getID(),
							new LinkedList<Action>());
				}
				hashMap.get(action.getVM().getID()).add(action);
			}
		}
		return hashMap;
	}

	/**
	 * Returns the first server which is off. Also sets its state as on.
	 * 
	 * @param dataCenter
	 * @return first server which is off
	 */
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

	private Server findServerToWakeUpForRack(DataCenter dataCenter2, Rack rack) {
		List<Server> allServers = dataCenter.getServerPool();
		for (Server server : allServers) {
			if ((server.getRack().getID() != rack.getID())
					&& (server.getRack().isOn()))
				if (server.getState().equals(ServerState.OFF.toString())) {
					server.setState(ServerState.ON.toString());
					return server;
				}
		}
		return null;
	}

	/**
	 * Iterate through all servers from the data center find server that is best
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
					if (!breaksPolicies(server, vm)) {
						bestRemainingRAMCapacity = availableRAMCapacity;
						bestRemainingCPUFrequency = availableCPUFrequency;
						bestServer = server;
					}
				}
			}
		}
		return bestServer; 
	}

	private Server findBestMatchingServerInRack(DataCenter dataCenter2,
			Rack rack, VirtualMachine vm) {
		List<Server> allServers = dataCenter.getServerPool();
		float bestRemainingResources = Float.MAX_VALUE;
		Server bestServer = null;
		for (Server server : allServers) {
			if (server.getRack().getID() != rack.getID()) {
				if (server.getState().equals(ServerState.ON.toString())) {
					float availableRAMCapacity = (server
							.getAvailableRAMCapacity() - vm.getRAM()
							.getCapacity())
							/ server.getRAM().getCapacity();
					float availableCPUFrequency = (server
							.getAvailableCPUFrequency() - vm.getCPU()
							.getTotalFrequency())
							/ server.getCPU().getTotalFrequency();
					assert (availableCPUFrequency <= 1.0 && availableRAMCapacity <= 1.0);
					// use euclidean distance
					float availableResources = availableRAMCapacity
							* availableRAMCapacity + availableCPUFrequency
							* availableCPUFrequency;
					if (availableResources < bestRemainingResources
							&& availableRAMCapacity > 0
							&& availableCPUFrequency > 0) {
						if (!breaksPolicies(server, vm)) {
							bestRemainingResources = availableResources;
							bestServer = server;
						}
					}
				}
			}
		}
		return bestServer;
	}

	// TODO change with respect to all server policies
	private boolean breaksPolicies(Server server, VirtualMachine vm) {
		float min_CPU = 20;
		float min_RAM = 30;
		float minCPU = min_CPU / 100 * server.getCPU().getTotalFrequency();
		float minRAM = min_RAM / 100 * server.getRAM().getCapacity();

		float availableCPUFrequency = server.getAvailableCPUFrequency()
				- vm.getCPU().getTotalFrequency();
		float availableRAMCapacity = server.getAvailableRAMCapacity()
				- vm.getRAM().getCapacity();

		return (availableCPUFrequency < minCPU || availableRAMCapacity < minRAM);
	}

	private Node generateLeaf(Node currentNode, Command command, Server src,
			Server dest, VirtualMachine vm, Rack rack) {
		Action action = null;

		float oldEntropy = currentNode.getEntropy();
		float oldReward = currentNode.getReward();
		switch (command) {
		case DEPLOY:
			action = new Deploy(src, dest, vm);
			break;
		case MIGRATE:
			action = new Migrate(src, dest, vm);
			break;
		case WAKEUP:
			action = new TurnOnServer(src, dest, vm);
			break;
		case SHUTDOWN:
			action = new TurnOffServer(src, dest, vm);
			break;
		case SHUTDOWN_RACK:
			action = new TurnOffRack(src, dest, vm, rack);
			break;
		case INTERCLOUDMIGRATE:
			action = new InterCloudMigration(src, dest, vm);
			break;
		}
		// 1. compute data center for current node
		SimulateAction simulation = new SimulateAction(dataCenter);
		List<Action> actions = new ArrayList<Action>();
		actions.addAll(currentNode.getActionSequence());
		// CHECK TO BE ADDED AT THE END
		actions.add(action);
		dataCenter = simulation.doAction(action);
		Evaluator evaluator = new Evaluator(dataCenter);
		float entropy = evaluator.computeEntropy(dataCenter);
		// undo actions on data center
		dataCenter = simulation.undoAction(action);

		Node newNode = new Node();
		newNode.setActionSequence(actions);
		newNode.setEntropy(entropy);
		// PAGINA 38 DELIVARABLE GAMES
		float reward = newNode.computeReward(oldReward, oldEntropy,
				action.getCost());
		newNode.setReward(reward);

		return newNode;
	}

	private PriorityQueue<Node> constructPriorityQueue(float oldEntropy) {
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
					actionSequence.add(action);
					break;
				} else {
					Server turnedOffServer = findServerToWakeUp(dataCenter);
					if (turnedOffServer != null) {
						Action turnOn = new TurnOnServer(turnedOffServer, null,
								null);
						Action deploy = new Deploy(null, turnedOffServer, vm);
						actionSequence.add(turnOn);
						actionSequence.add(deploy);
						break;
					}
				}
			} else if (subject instanceof Server) {
				Server server = (Server) subject;
				if (server.getRunningVMs().isEmpty()) {
					Action action = new TurnOffServer(server, null, null);
					actionSequence.add(action);
					break;
				} else {
					VirtualMachine vm = server.getRunningVMs().get(
							server.getRunningVMs().size() - 1);
					Server destinationServer = this.findBestMatchingServer(
							dataCenter, vm);
					if (destinationServer == null) {
						destinationServer = findServerToWakeUp(dataCenter);
						if (destinationServer != null) {
							Action action = new Migrate(server,
									destinationServer, vm);
							actionSequence.add(action);
							break;
						} else {
							Action action = new InterCloudMigration(null, null,
									vm);
							actionSequence.add(action);
							break;
						}
					} else {
						Action action = new Migrate(server, destinationServer,
								vm);
						actionSequence.add(action);
						break;
					}
				}
			}
		}
		Node root = constructRoot(oldEntropy, evaluator, actionSequence);
		PriorityQueue<Node> queue = new PriorityQueue<Node>();
		queue.add(root);
		return queue;
	}

	private Node constructRoot(float oldEntropy, Evaluator evaluator,
			List<Action> actionSequence) {
		Node root = new Node();
		SimulateAction simulation = new SimulateAction(dataCenter);
		dataCenter = simulation.doActions(actionSequence);
		float entropy = evaluator.computeEntropy(dataCenter);
		dataCenter = simulation.undoActions(actionSequence);
		root.setEntropy(entropy);
		root.setActionSequence(actionSequence);
		root.setReward(root.computeReward(0, oldEntropy, actionSequence.get(0)
				.getCost()));
		return root;
	}
}
