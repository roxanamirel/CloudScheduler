package planning;

import database.model.Server;
import database.model.VirtualMachine;

public class Migrate extends Action
{

	public Migrate(Server sourceServerID, Server destinationServerID, VirtualMachine vMID) {
		super(sourceServerID, destinationServerID, vMID);
	}
}
