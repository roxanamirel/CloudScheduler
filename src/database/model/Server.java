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
	@OneToOne(cascade = CascadeType.PERSIST)
	private DataCenter dataCenter;
	@OneToOne(cascade = CascadeType.PERSIST)
	private Rack rack;
	
	@OneToMany(mappedBy = "host",cascade  = CascadeType.ALL,fetch=FetchType.EAGER )
	private List<VirtualMachine> runningVMs = new ArrayList<VirtualMachine>();

	public Server(){}
	
	
	public Server(CPU cPU, RAM rAM,HDD hDD) {
		CPU = cPU;
		RAM = rAM;
		HDD = hDD;
	}


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
	
	/**
	 * @return the rack
	 */
	public Rack getRack() {
		return rack;
	}


	/**
	 * @param rack the rack to set
	 */
	public void setRack(Rack rack) {
		this.rack = rack;
	}


	public float getAvailableCPUFrequency(){
		float avaiableCPUFrequency = this.CPU.getTotalFrequency();
		for(VirtualMachine vm : this.runningVMs){
			avaiableCPUFrequency -= vm.getCPU().getTotalFrequency();
		}
		return avaiableCPUFrequency;
	}
	
	public float getAvailableRAMCapacity() {
		float avaiableRAMCapacity = this.RAM.getCapacity();
		for(VirtualMachine vm : this.runningVMs){
			avaiableRAMCapacity -= vm.getRAM().getCapacity();
		}
		return avaiableRAMCapacity;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((CPU == null) ? 0 : CPU.hashCode());
		result = prime * result + ((HDD == null) ? 0 : HDD.hashCode());
		result = prime * result + ID;
		result = prime * result + ((RAM == null) ? 0 : RAM.hashCode());
		result = prime * result
				+ ((dataCenter == null) ? 0 : dataCenter.hashCode());
		result = prime * result
				+ ((runningVMs == null) ? 0 : runningVMs.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Server other = (Server) obj;
		if (CPU == null) {
			if (other.CPU != null)
				return false;
		} else if (!CPU.equals(other.CPU))
			return false;
		if (HDD == null) {
			if (other.HDD != null)
				return false;
		} else if (!HDD.equals(other.HDD))
			return false;
		if (ID != other.ID)
			return false;
		if (RAM == null) {
			if (other.RAM != null)
				return false;
		} else if (!RAM.equals(other.RAM))
			return false;
		if (dataCenter == null) {
			if (other.dataCenter != null)
				return false;
		} else if (!dataCenter.equals(other.dataCenter))
			return false;
		if (runningVMs == null) {
			if (other.runningVMs != null)
				return false;
		} else if (!runningVMs.equals(other.runningVMs))
			return false;
		return true;
	}
	
	

}
