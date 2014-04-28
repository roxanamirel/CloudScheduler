package planning;


import initializations.PolicyPool;

import java.util.List;


import database.model.DataCenter;
import database.model.Server;
import database.model.VirtualMachine;

public class Deploy extends Action {

	public Deploy(Server sourceServerID, Server destinationServerID,
			VirtualMachine vMID) {
		super(sourceServerID, destinationServerID, vMID);
	}

	@Override
	public DataCenter Do(DataCenter dc) {
		VirtualMachine vm = this.getVM();
		Server server = this.getFacadeFactory().createServerFacade().find(
				this.getDestinationServer().getID());
		vm.setHost(server);
		vm = this.getFacadeFactory().createVirtualMachineFacade().update(vm);
		this.setVM(vm);
		List<VirtualMachine> vms = server.getRunningVMs();
		vms.add(this.getVM());
		server.setRunningVMs(vms);
		server = this.getFacadeFactory().createServerFacade().update(server);
		dc = this.getFacadeFactory().createDataCenterFacade().find(dc.getID());
		dc.setPolicyPool(PolicyPool.getPolicyPool());
		return dc;
	}

	@Override
	public DataCenter Undo(DataCenter dc) {
		VirtualMachine vm = this.getVM();
		Server server = this.getFacadeFactory().createServerFacade().find(
				this.getDestinationServer().getID());
		vm.setHost(null);
		vm = this.getFacadeFactory().createVirtualMachineFacade().update(vm);
		this.setVM(vm);
		List<VirtualMachine> vms = server.getRunningVMs();
		vms.remove(this.getVM());
		server.setRunningVMs(vms);
		server = this.getFacadeFactory().createServerFacade().update(server);
		dc = this.getFacadeFactory().createDataCenterFacade().find(dc.getID());
		dc.setPolicyPool(PolicyPool.getPolicyPool());
		return dc;
	}
}
