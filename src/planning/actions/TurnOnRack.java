package planning.actions;

import util.ServerState;
import database.model.DataCenter;
import database.model.Rack;
import database.model.Server;
import database.model.VirtualMachine;

public class TurnOnRack extends Action {
	private static final float COST = 600;
	private Rack rack;

	public TurnOnRack(Server sourceServer, Server destinationServer,
			VirtualMachine vm, Rack rack) {
		super(sourceServer, destinationServer, vm, COST
				* rack.getServers().size());
		this.rack = rack;
		// this.COST = 600*rack.getServers().size();
	}

	@Override
	public DataCenter Do(DataCenter dc) {
		for (Rack r : dc.getRack()) {
			if (rack.getID() == r.getID()) {
				r.setState(ServerState.ON.toString());
			}
		}
		return dc;
	}

	@Override
	public DataCenter Undo(DataCenter dc) {
		for (Rack r : dc.getRack()) {
			if (rack.getID() == r.getID()) {
				r.setState(ServerState.OFF.toString());
			}
		}
		return dc;
	}
}
