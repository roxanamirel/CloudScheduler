package database.facade;

import java.util.List;
import database.dao.VirtualMachineDAO;
import database.model.VirtualMachine;

public class VirtualMachineFacadeImpl implements VirtualMachineFacade {

	private VirtualMachineDAO vmDAO;

	public VirtualMachineFacadeImpl(VirtualMachineDAO vmDAO) {
		this.vmDAO = vmDAO;
	}

	@Override
	public void save(VirtualMachine vm) {
		this.vmDAO.save(vm);

	}

	@Override
	public VirtualMachine update(VirtualMachine vm) {
		return this.vmDAO.update(vm);
	}

	@Override
	public void delete(VirtualMachine vm) {
		this.vmDAO.delete(vm.getID(), VirtualMachine.class);
	}

	@Override
	public VirtualMachine find(int entityID) {
		return this.vmDAO.find(entityID);
	}

	@Override
	public List<VirtualMachine> findAll() {
		return vmDAO.findAll();
	}

}
