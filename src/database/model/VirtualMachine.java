package database.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "VirtualMachine")
public class VirtualMachine extends ApplicationResource {
	@Id
	private int ID;

	@OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
	private CPU CPU;

	@OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
	private RAM RAM;

	@OneToOne(cascade = CascadeType.PERSIST)
	private DataCenter dataCenter;

	@OneToOne(cascade = CascadeType.PERSIST)
	private Server host;

	private HDD HDD;

	public VirtualMachine() {
	}

	public VirtualMachine(database.model.CPU cPU, database.model.RAM rAM,
			database.model.HDD hDD) {
		CPU = cPU;
		RAM = rAM;
		HDD = hDD;
	}

	public VirtualMachine(int iD, database.model.CPU cPU,
			database.model.RAM rAM, DataCenter dataCenter, Server host,
			database.model.HDD hDD) {
		ID = iD;
		CPU = cPU;
		RAM = rAM;
		this.dataCenter = dataCenter;
		this.host = host;
		HDD = hDD;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VirtualMachine [ID=" + ID + "]";
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public void setID(int id) {
		this.ID = id;
	}

	/**
	 * @return the cPU
	 */
	public CPU getCPU() {
		return CPU;
	}

	/**
	 * @param cPU
	 *            the cPU to set
	 */
	public void setCPU(CPU cPU) {
		CPU = cPU;
	}

	/**
	 * @return the rAM
	 */
	public RAM getRAM() {
		return RAM;
	}

	/**
	 * @param rAM
	 *            the rAM to set
	 */
	public void setRAM(RAM rAM) {
		RAM = rAM;
	}

	/**
	 * @return the hDD
	 */
	public HDD getHDD() {
		return HDD;
	}

	/**
	 * @param hDD
	 *            the hDD to set
	 */
	public void setHDD(HDD hDD) {
		HDD = hDD;
	}

	/**
	 * @return the host
	 */
	public Server getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(Server host) {
		this.host = host;
	}

	/**
	 * @return the dataCenter
	 */
	public DataCenter getDataCenter() {
		return dataCenter;
	}

	/**
	 * @param dataCenter
	 *            the dataCenter to set
	 */
	public void setDataCenter(DataCenter dataCenter) {
		this.dataCenter = dataCenter;
	}

}
