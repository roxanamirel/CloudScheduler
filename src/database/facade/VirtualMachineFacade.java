package database.facade;

import java.util.List;
import database.model.VirtualMachine;

public interface VirtualMachineFacade {

	public abstract void save(VirtualMachine vm);

	public abstract VirtualMachine update(VirtualMachine vm);

	public abstract void delete(VirtualMachine vm);

	public abstract VirtualMachine find(int entityID);

	public abstract List<VirtualMachine> findAll();

}
