package database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Server")
public class Server {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;
private int ram;


/**
 * @return the id
 */
public int getId() {
	return id;
}

/**
 * @param id the id to set
 */
public void setId(int id) {
	this.id = id;
}

/**
 * @return the ram
 */
public int getRam() {
	return ram;
}

/**
 * @param ram the ram to set
 */
public void setRam(int ram) {
	this.ram = ram;
}

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Override
public String toString() {
	return "Server [id=" + id + ", ram=" + ram + "]";
}





}
