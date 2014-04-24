package database.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "VirtualMachine")
public class VirtualMachine extends ApplicationResource {
	@Id
	private int ID;
	@OneToOne(cascade = CascadeType.PERSIST)
	private CPU CPU;
	@OneToOne(cascade = CascadeType.PERSIST)
	private RAM RAM;
	private HDD HDD;
	private Server host;

	public VirtualMachine() {
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

}
