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
		updateVirtualMachine();		
		updateServers();		
		return updateDataCenter(dc);	
	}

	@Override
	public DataCenter Undo(DataCenter dc) {
		return null;
	}
	
	private void updateVirtualMachine() {
		Server server = this.getFacadeFactory().createServerFacade().find(
				this.getDestinationServer().getID());
		VirtualMachine vm = this.getVM();
		vm.setHost(server);
		vm = this.getFacadeFactory().createVirtualMachineFacade().update(vm);
		this.setVM(vm);		
	}
	
	private void updateServers() {
		updateDestinationServer();
		updateSourceServer();
	}
	
	private void updateDestinationServer() {
		Server server = this.getFacadeFactory().createServerFacade().find(
				this.getDestinationServer().getID());
		List<VirtualMachine> vms = server.getRunningVMs();
		vms.add(this.getVM());
		server.setRunningVMs(vms);
		server = this.getFacadeFactory().createServerFacade().update(server);		
	}
	
	private void updateSourceServer() {
		Server server = this.getFacadeFactory().createServerFacade().find(
				this.getSourceServer().getID());
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
