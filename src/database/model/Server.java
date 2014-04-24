package database.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Server")
public class Server extends ITComputingResource {
	@Id
	private int ID;
	@OneToOne(cascade = CascadeType.PERSIST)
	private CPU CPU;
	@OneToOne(cascade = CascadeType.PERSIST)
	private RAM RAM;
	@OneToOne(cascade = CascadeType.PERSIST)
	private HDD HDD;
	@OneToMany(mappedBy = "host",cascade  = CascadeType.PERSIST)
	private List<VirtualMachine> runningVMs;

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public void setID(int id) {
	this.ID =id;
		
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
	 * @return the runningVMs
	 */
	public List<VirtualMachine> getRunningVMs() {
		return runningVMs;
	}

	/**
	 * @param runningVMs
	 *            the runningVMs to set
	 */
	public void setRunningVMs(List<VirtualMachine> runningVMs) {
		this.runningVMs = runningVMs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Server [CPU=" + CPU + ", RAM=" + RAM + ", HDD=" + HDD
				+ ", runningVMs=" + runningVMs + "]";
	}

}
