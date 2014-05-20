package planning.actions;

import java.util.Iterator;

import database.model.DataCenter;
import database.model.Server;
import database.model.VirtualMachine;

public class InterCloudMigration extends Action {

	private static final float COST = 1000;
	
	public InterCloudMigration(Server sourceServer, Server destinationServer,
			VirtualMachine vm) {
		super(sourceServer, destinationServer, vm, COST);
	}

	@Override
	public DataCenter Do(DataCenter dc) {
		VirtualMachine vm = this.getVM();
		Iterator<VirtualMachine> iterator = dc.getVMPool().iterator();

		while (iterator.hasNext()) {
			if (vm.getID() == iterator.next().getID()) {
				iterator.remove();
			}
		}

		return dc;
	}

	@Override
	public DataCenter Undo(DataCenter dc) {
		VirtualMachine vm = this.getVM();
		dc.getVMPool().add(vm);
		return dc;
	}

}
