package planning;

import initializations.PolicyPool;
import util.ServerState;
import database.model.DataCenter;
import database.model.Server;
import database.model.VirtualMachine;

public class TurnOnServer extends Action
{

	public TurnOnServer(Server sourceServerID, Server destinationServerID, VirtualMachine vMID) {
		super(sourceServerID, destinationServerID, vMID);
	}

	@Override
	public DataCenter Do(DataCenter dc) {
		/*Server server = this.getFacadeFactory().createServerFacade().find(
				this.getSourceServer().getID());		
		server.setState(ServerState.ON.toString());
		this.getFacadeFactory().createServerFacade().update(server);
		dc = this.getFacadeFactory().createDataCenterFacade().find(dc.getID());
		dc.setPolicyPool(PolicyPool.getPolicyPool());
		return dc;*/
		Server server = this.getSourceServer();		
		for(Server s:dc.getServerPool()){
			if(s.getID()==server.getID()){
				s.setState(ServerState.ON.toString());
			}
		}
		return dc;
	}

	@Override
	public DataCenter Undo(DataCenter dc) {
		/*Server server = this.getFacadeFactory().createServerFacade().find(
				this.getSourceServer().getID());		
		server.setState(ServerState.OFF.toString());
		this.getFacadeFactory().createServerFacade().update(server);
		dc = this.getFacadeFactory().createDataCenterFacade().find(dc.getID());
		dc.setPolicyPool(PolicyPool.getPolicyPool());
		return dc;*/
		Server server = this.getSourceServer();		
		for(Server s:dc.getServerPool()){
			if(s.getID()==server.getID()){
				s.setState(ServerState.OFF.toString());
			}
		}
		return dc;
	}
}
