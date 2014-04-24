package planning;

import database.model.Server;
import database.model.VirtualMachine;

public class TurnOnServer extends Action
{

	public TurnOnServer(Server sourceServerID, Server destinationServerID, VirtualMachine vMID) {
		super(sourceServerID, destinationServerID, vMID);
	}
}
