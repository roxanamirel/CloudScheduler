package database.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import reasoning.policies.Policy;

@Entity
@Table(name = "DataCenter")
public class DataCenter
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;
	
	@OneToMany(mappedBy = "dataCenter", cascade = CascadeType.ALL)
	private List<Server> serverPool;
	
	@OneToMany(mappedBy = "dataCenter", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private List<VirtualMachine> VMPool;
	
	private List<Policy> policyPool;
	
	public List<Resource> getAllResources()
	{
		ArrayList<Resource> all = new ArrayList<Resource>();
		all.addAll(VMPool);
		all.addAll(serverPool);
		return all;
	
	}
	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}
	/**
	 * @param iD the iD to set
	 */
	public void setID(int iD) {
		ID = iD;
	}
	/**
	 * @return the serverPool
	 */
	public List<Server> getServerPool() {
		return serverPool;
	}
	/**
	 * @param serverPool the serverPool to set
	 */
	public void setServerPool(List<Server> serverPool) {
		this.serverPool = serverPool;
	}
	/**
	 * @return the vMPool
	 */
	public List<VirtualMachine> getVMPool() {
		return VMPool;
	}
	/**
	 * @param vMPool the vMPool to set
	 */
	public void setVMPool(List<VirtualMachine> vMPool) {
		VMPool = vMPool;
	}
	/**
	 * @return the policyPool
	 */
	public List<Policy> getPolicyPool() {
		return policyPool;
	}
	/**
	 * @param policyPool the policyPool to set
	 */
	public void setPolicyPool(List<Policy> policyPool) {
		this.policyPool = policyPool;
	}
}
