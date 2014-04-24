package planning;

import database.model.Server;
import database.model.VirtualMachine;

public class Deploy extends Action
{

	public Deploy(Server sourceServerID, Server destinationServerID, VirtualMachine vMID) {
		super(sourceServerID, destinationServerID, vMID);
	}
}
