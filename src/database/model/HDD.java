package database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HDD")
public class HDD extends ITComputingResource {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;
	private float capacity;
	@Override
	public int getID() {
		return this.ID;
	}

	@Override
	public void setID(int id) {
		this.ID = id;

	}

	public HDD() {
	}

	public float getCapacity() {
		return capacity;
	}

	public void setCapacity(float capacity) {
		this.capacity = capacity;
	}

}
