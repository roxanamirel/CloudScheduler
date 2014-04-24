package monitoring.util;

import javax.persistence.EntityManager;

import database.connection.DBConnection;
import database.dao.DataCenterDAO;
import database.dao.ServerDAO;
import database.dao.VirtualMachineDAO;
import database.facade.DataCenterFacade;
import database.facade.DataCenterFacadeImpl;
import database.facade.ServerFacade;
import database.facade.ServerFacadeImpl;
import database.facade.VirtualMachineFacade;
import database.facade.VirtualMachineFacadeImpl;

public class FacadeFactory {
	
	private EntityManager entityManager = DBConnection.connect(); 

	public ServerFacade createServerFacade() {
		return new ServerFacadeImpl(new ServerDAO(entityManager));
	}
	
	public VirtualMachineFacade createVirtualMachineFacade() {
		return new VirtualMachineFacadeImpl(new VirtualMachineDAO(entityManager));
	}
	
	public DataCenterFacade createDataCenterFacade() {
		return new DataCenterFacadeImpl(new DataCenterDAO(entityManager));
	}
	
	public void closeConnection() {
		entityManager.close();
	}
}
