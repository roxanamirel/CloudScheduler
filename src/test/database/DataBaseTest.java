package test.database;

import static org.junit.Assert.*;

import java.util.List;

import monitoring.util.FacadeFactory;

import org.junit.Test;

import database.model.DataCenter;
import database.model.VirtualMachine;

public class DataBaseTest {

	@Test
	public void test() {
		FacadeFactory facadeFactory = new FacadeFactory();
		DataCenter dataCenter = facadeFactory.createDataCenterFacade()
				.findAll().get(0);
		List<VirtualMachine> virtualMachines = dataCenter.getVMPool();
		
		int noOfVMS = virtualMachines.size();
		
		VirtualMachine vm = virtualMachines.get(0);
		
		facadeFactory.createVirtualMachineFacade().delete(vm);
	
		
		assertEquals(noOfVMS - 1, facadeFactory.createDataCenterFacade()
				.findAll().get(0).getVMPool().size());
	}
	
	
}
