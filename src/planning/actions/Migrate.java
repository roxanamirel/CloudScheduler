package planning.actions;

import database.model.DataCenter;
import database.model.Server;
import database.model.VirtualMachine;

public class Migrate extends Action {
	
	private static final float COST = 400;
	
	public Migrate(Server sourceServer, Server destinationServer,
			VirtualMachine vm) {
		super(sourceServer, destinationServer, vm, COST);
	}

	@Override
	public DataCenter Do(DataCenter dc) {
		Server destServer = this.getDestinationServer();
		Server sourceServer = this.getSourceServer();
		updateVirtualMachine(destServer, dc);
		updateDestinationServer(destServer, dc);
		updateSourceServer(sourceServer, dc);
		return dc;
	}

	@Override
	public DataCenter Undo(DataCenter dc) {
		Server destServer = this.getDestinationServer();
		Server sourceServer = this.getSourceServer();
		updateVirtualMachine(sourceServer, dc);
		updateSourceServer(destServer, dc);
		updateDestinationServer(sourceServer, dc);
		return dc;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Migrate " + getVM() + " from " + getSourceServer() + " to "
				+ getDestinationServer();
	}

	private void updateVirtualMachine(Server server, DataCenter dc) {
		this.getVM().setHost(server);
		for (VirtualMachine virtualMachine : dc.getVMPool()) {
			if (virtualMachine.getID() == this.getVM().getID()) {
				virtualMachine.setHost(server);
			}
		}
	}

	private void updateDestinationServer(Server server, DataCenter dc) {
		for (Server serv : dc.getServerPool()) {
			if (server.getID() == serv.getID()) {
				server.getRunningVMs().add(this.getVM());
			}
		}
	}

	private void updateSourceServer(Server server, DataCenter dc) {
		for (Server serv : dc.getServerPool()) {
			if (server.getID() == serv.getID()) {
				server.getRunningVMs().remove(this.getVM());
			}
		}
	}
}
