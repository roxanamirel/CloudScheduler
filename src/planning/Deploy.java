package planning;


import initializations.PolicyPool;

import java.util.List;


import database.model.DataCenter;
import database.model.Server;
import database.model.VirtualMachine;

public class Deploy extends Action {

	public Deploy(Server sourceServer, Server destinationServer,
			VirtualMachine vm) {
		super(sourceServer, destinationServer, vm);
	}

	@Override
	public DataCenter Do(DataCenter dc) {
		
		VirtualMachine vm = this.getVM();
		Server destinationServer = this.getDestinationServer();		
		vm.setHost(destinationServer);		
		destinationServer.getRunningVMs().add(vm);
		
		for(Server server : dc.getServerPool()) {
			if (server.getID() == destinationServer.getID()) {
				server.getRunningVMs().add(vm);
			}
		}
		
		for(VirtualMachine vMachine : dc.getVMPool()) {
			if (vMachine.getID() == vm.getID()) {
				vMachine.setHost(destinationServer);
			}
		}
		
		
		
//		VirtualMachine vm = this.getVM();
//		Server server = this.getFacadeFactory().createServerFacade().find(
//				this.getDestinationServer().getID());
//		vm.setHost(server);
		
		
		
//		vm = this.getFacadeFactory().createVirtualMachineFacade().update(vm);
//		this.setVM(vm);
//		List<VirtualMachine> vms = server.getRunningVMs();
//		vms.add(this.getVM());
//		server.setRunningVMs(vms);
//		server = this.getFacadeFactory().createServerFacade().update(server);
//		dc = this.getFacadeFactory().createDataCenterFacade().find(dc.getID());
//		dc.setPolicyPool(PolicyPool.getPolicyPool());
		return dc;
	}

	@Override
	public DataCenter Undo(DataCenter dc) {
		
		VirtualMachine vm = this.getVM();
		Server destinationServer = this.getDestinationServer();
		
		vm.setHost(null);		
		destinationServer.getRunningVMs().remove(vm);
		
		for(Server server : dc.getServerPool()) {
			if (server.getID() == destinationServer.getID()) {
				server.getRunningVMs().remove(vm);
			}
		}
		
		for(VirtualMachine vMachine : dc.getVMPool()) {
			if (vMachine.getID() == vm.getID()) {
				vMachine.setHost(null);
			}
		}
		
//		VirtualMachine vm = this.getVM();
//		Server server = this.getFacadeFactory().createServerFacade().find(
//				this.getDestinationServer().getID());
//		vm.setHost(null);
//		vm = this.getFacadeFactory().createVirtualMachineFacade().update(vm);
//		this.setVM(vm);
//		List<VirtualMachine> vms = server.getRunningVMs();
//		vms.remove(this.getVM());
//		server.setRunningVMs(vms);
//		server = this.getFacadeFactory().createServerFacade().update(server);
//		dc = this.getFacadeFactory().createDataCenterFacade().find(dc.getID());
//		dc.setPolicyPool(PolicyPool.getPolicyPool());
		return dc;
	}
}
