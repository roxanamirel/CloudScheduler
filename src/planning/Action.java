package planning;

import database.model.DataCenter;
import database.model.Server;
import database.model.VirtualMachine;

public class Action
{
	private Server SourceServerID;	
	private Server DestinationServerID;	
	private VirtualMachine VMID;	
	private float cost;
	
	public Action(Server sourceServerID, Server destinationServerID, VirtualMachine vMID) {
		super();
		SourceServerID = sourceServerID;
		DestinationServerID = destinationServerID;
		VMID = vMID;
	}

	/** */
	public DataCenter Do(DataCenter dc)
	{
		//do action in data center
		//example: move VM for source server to destination server
		return dc;
	}
	
	/** */
	public DataCenter Undo(DataCenter dc)
	{
		//undo action in data center
		return dc;	
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	/**
	 * @return the sourceServerID
	 */
	public Server getSourceServerID() {
		return SourceServerID;
	}

	/**
	 * @param sourceServerID the sourceServerID to set
	 */
	public void setSourceServerID(Server sourceServerID) {
		SourceServerID = sourceServerID;
	}

	/**
	 * @return the destinationServerID
	 */
	public Server getDestinationServerID() {
		return DestinationServerID;
	}

	/**
	 * @param destinationServerID the destinationServerID to set
	 */
	public void setDestinationServerID(Server destinationServerID) {
		DestinationServerID = destinationServerID;
	}

	/**
	 * @return the vMID
	 */
	public VirtualMachine getVMID() {
		return VMID;
	}

	/**
	 * @param vMID the vMID to set
	 */
	public void setVMID(VirtualMachine vMID) {
		VMID = vMID;
	}
}
