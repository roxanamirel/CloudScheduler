package planning.actions;

import database.model.DataCenter;
import database.model.Server;
import database.model.VirtualMachine;

public class InterCloudMigration extends Action{

	public InterCloudMigration(Server sourceServer, Server destinationServer,
			VirtualMachine vm) {
		super(sourceServer, destinationServer, vm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public DataCenter Do(DataCenter dc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataCenter Undo(DataCenter dc) {
		// TODO Auto-generated method stub
		return null;
	}

}
