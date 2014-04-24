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
	private List<CPUCore> CPU;

	public CPU() {
	}

	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public void setID(int id) {
		this.ID = id;
	}

	public List<CPUCore> getCPU() {
		return CPU;
	}

	public void setCPU(List<CPUCore> cPU) {
		CPU = cPU;
	}

	public float getAllCPU() {
		Iterator<CPUCore> it = CPU.iterator();
		float cpu = 0;
		while (it.hasNext()) {
			CPUCore aux = it.next();
			cpu += aux.getFrequency() * aux.getWeight();
		}

		return cpu;
	}

}
