package planning;

import database.model.Server;
import database.model.VirtualMachine;



public class TurnOffServer extends Action
{

	public TurnOffServer(Server sourceServerID, Server destinationServerID, VirtualMachine vMID) {
		super(sourceServerID, destinationServerID, vMID);
	}
}
