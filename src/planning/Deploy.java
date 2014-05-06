package planning;

import database.model.DataCenter;
import database.model.Server;
import database.model.VirtualMachine;

public class Deploy extends Action {
	
	private static final float COST = 200;

	public Deploy(Server sourceServer, Server destinationServer,
			VirtualMachine vm) {
		super(sourceServer, destinationServer, vm, COST);
	}

	@Override
	public DataCenter Do(DataCenter dc) {

		VirtualMachine vm = this.getVM();
		Server destinationServer = this.getDestinationServer();
		vm.setHost(destinationServer);
		Server serverDest = null;
		for (Server server : dc.getServerPool()) {
			if (server.getID() == destinationServer.getID()) {
				server.getRunningVMs().add(vm);
				serverDest = server;
			}
		}

		for (VirtualMachine vMachine : dc.getVMPool()) {
			if (vMachine.getID() == vm.getID()) {
				vMachine.setHost(serverDest);
				break;
			}
		}

		return dc;
	}

	@Override
	public DataCenter Undo(DataCenter dc) {

		VirtualMachine vm = this.getVM();
		Server destinationServer = this.getDestinationServer();

		vm.setHost(null);
		for (Server server : dc.getServerPool()) {
			if (server.getID() == destinationServer.getID()) {
				server.getRunningVMs().remove(vm);
			}
		}

		for (VirtualMachine vMachine : dc.getVMPool()) {
			if (vMachine.getID() == vm.getID()) {
				vMachine.setHost(null);
			}
		}
		return dc;
	}
}
