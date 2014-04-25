package database.model;

import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "CPU")
public class CPU extends ITComputingResource {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;
	@OneToMany(mappedBy = "cpu", cascade = CascadeType.PERSIST)
	private List<CPUCore> cores;

	public CPU() {
	}

	
	public CPU(List<CPUCore> cores) {
		this.cores = cores;
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
	 * @return the cores
	 */
	public List<CPUCore> getCores() {
		return cores;
	}


	/**
	 * @param cores the cores to set
	 */
	public void setCores(List<CPUCore> cores) {
		this.cores = cores;
	}


	public float getTotalFrequency() {
		Iterator<CPUCore> it = cores.iterator();
		float cpu = 0;
		while (it.hasNext()) {
			CPUCore core = it.next();
			cpu += core.getFrequency() * core.getWeight();
		}
		return cpu;
	}

}
