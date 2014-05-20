package database.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import util.ServerState;

@Entity
@Table(name = "Rack")
public class Rack extends ITComputingResource {
	@Id
	private int ID;
	@OneToMany(mappedBy = "rack", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Server> servers = new ArrayList<Server>();
	@OneToOne(cascade = CascadeType.PERSIST)
	private DataCenter dataCenter;
	private int capacity;
	
	//needed for persistence!
	public Rack(){}
	public Rack(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setID(int iD) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the servers
	 */
	public List<Server> getServers() {
		return servers;
	}

	/**
	 * @param servers
	 *            the servers to set
	 */
	public void setServers(List<Server> servers) {
		this.servers = servers;
	}

	/**
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * @param capacity the capacity to set
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	/**
	 * @return the state
	 */
	public boolean isOn() {
		for(Server server:this.getServers()){
			if(server.getState().toString().equals(ServerState.ON)) return true;
		}
		return false;
	}
	
	/**
	 * @return the dataCenter
	 */
	public DataCenter getDataCenter() {
		return dataCenter;
	}
	/**
	 * @param dataCenter the dataCenter to set
	 */
	public void setDataCenter(DataCenter dataCenter) {
		this.dataCenter = dataCenter;
	}
	
	
}
