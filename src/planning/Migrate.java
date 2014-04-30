package planning;

import initializations.PolicyPool;
import java.util.List;
import database.model.DataCenter;
import database.model.Server;
import database.model.VirtualMachine;

public class Migrate extends Action
{
	public Migrate(Server sourceServer, Server destinationServer, VirtualMachine vm) {
		super(sourceServer, destinationServer, vm);
	}

	@Override
	public DataCenter Do(DataCenter dc) {
		Server destServer = this.getFacadeFactory().createServerFacade().find(
				this.getDestinationServer().getID());
		Server sourceServer = this.getFacadeFactory().createServerFacade().find(
				this.getSourceServer().getID());
		updateVirtualMachine(destServer);	
		updateDestinationServer(destServer);
		updateSourceServer(sourceServer);		
		return updateDataCenter(dc);	
	}

	@Override
	public DataCenter Undo(DataCenter dc) {
		Server destServer = this.getFacadeFactory().createServerFacade().find(
				this.getDestinationServer().getID());
		Server sourceServer = this.getFacadeFactory().createServerFacade().find(
				this.getSourceServer().getID());
		updateVirtualMachine(sourceServer);	
		updateVirtualMachine(sourceServer);	
		updateSourceServer(destServer);
		updateDestinationServer(sourceServer);		
		return updateDataCenter(dc);	
	}
	
	private void updateVirtualMachine(Server server) {
		VirtualMachine vm = this.getVM();
		vm.setHost(server);
		vm = this.getFacadeFactory().createVirtualMachineFacade().update(vm);
		this.setVM(vm);		
	}	
	
	private void updateDestinationServer(Server server) {
		List<VirtualMachine> vms = server.getRunningVMs();
		vms.add(this.getVM());
		server.setRunningVMs(vms);
		server = this.getFacadeFactory().createServerFacade().update(server);		
	}
	
	private void updateSourceServer(Server server) {
		List<VirtualMachine> vms = server.getRunningVMs();
		vms.remove(this.getVM());
		server.setRunningVMs(vms);
		server = this.getFacadeFactory().createServerFacade().update(server);
	}
	
	private DataCenter updateDataCenter(DataCenter dataCenter) {
		dataCenter = this.getFacadeFactory().createDataCenterFacade().find(dataCenter.getID());
		dataCenter.setPolicyPool(PolicyPool.getPolicyPool());
		return dataCenter;		
	}	
}
