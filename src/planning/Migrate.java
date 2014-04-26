package planning;

import database.model.DataCenter;
import database.model.Server;
import database.model.VirtualMachine;

public class Migrate extends Action
{

	public Migrate(Server sourceServerID, Server destinationServerID, VirtualMachine vMID) {
		super(sourceServerID, destinationServerID, vMID);
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
