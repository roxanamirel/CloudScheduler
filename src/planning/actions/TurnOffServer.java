package planning.actions;

import util.ServerState;
import database.model.DataCenter;
import database.model.Server;
import database.model.VirtualMachine;

public class TurnOffServer extends Action {

	private static final float COST = 0;

	public TurnOffServer(Server sourceServer, Server destinationServerID,
			VirtualMachine vMID) {
		super(sourceServer, destinationServerID, vMID, COST);
	}

	@Override
	public DataCenter Do(DataCenter dc) {
		Server server = this.getSourceServer();
		for (Server s : dc.getServerPool()) {
			if (s.getID() == server.getID()) {
					s.setState(ServerState.OFF.toString());
			}
		}
		return dc;
	}

	@Override
	public DataCenter Undo(DataCenter dc) {
		Server server = this.getSourceServer();
		for (Server s : dc.getServerPool()) {
			if (s.getID() == server.getID()) {
				s.setState(ServerState.ON.toString());
			}
		}
		return dc;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TurnOffServer " + getSourceServer();
	}	
}
