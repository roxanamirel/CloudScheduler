package database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CPUCore")
public class CPUCore extends ITComputingResource {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;
	private CPU cpu;
	private float frequency;
	private float weight;

	public CPUCore() {
	}
	
	

	public CPUCore(float frequency, float weight) {
		this.frequency = frequency;
		this.weight = weight;
	}



	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public void setID(int id) {
		this.ID = id;

	}

	public float getFrequency() {
		return frequency;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * @return the cpu
	 */
	public CPU getCpu() {
		return cpu;
	}

	/**
	 * @param cpu
	 *            the cpu to set
	 */
	public void setCpu(CPU cpu) {
		this.cpu = cpu;
	}

}
