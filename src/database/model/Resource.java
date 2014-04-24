package database.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Resource
{
	@Column
	private String name;
	@Column
	private String state;
	/**
	 * @return the iD
	 */
	
	
	public abstract int getID();
	/**
	 * @param iD the iD to set
	 */
	public abstract void setID(int iD);
	/**
	 * @return the name
	 */
	
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Resource [ID=" +  getID() + ", name=" + name + ", state=" + state
				+ "]";
	}
	
	
}
