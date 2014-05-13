package database.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Rack")
public class Rack extends ITComputingResource {
	@Id
	private int ID;
	@OneToMany(mappedBy = "rack", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Server> servers = new ArrayList<Server>();

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

}
