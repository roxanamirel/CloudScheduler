package database.dao;

import database.model.VirtualMachine;

public class VirtualMachineDAO extends GenericDAO<VirtualMachine> {

	public VirtualMachineDAO(Class<VirtualMachine> entityClass) {
		super(entityClass);
	}

}
