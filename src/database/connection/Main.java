package database.connection;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import database.dao.ServerDAO;
import database.dao.VirtualMachineDAO;
import database.facade.ServerFacade;
import database.facade.ServerFacadeImpl;
import database.facade.VirtualMachineFacade;
import database.facade.VirtualMachineFacadeImpl;
import database.model.Server;
import database.model.VirtualMachine;

public class Main {

	public static void main(String[] args) {

		EntityManager em = DBConnection.connect();
		// read the existing entries and write to console
		// Query q = em.createQuery("select t from Server t");
		// List<Server> todoList = q.getResultList();
		// for (Server todo : todoList) {
		// System.out.println(todo);
		// }
		// System.out.println("Size: " + todoList.size());

		// create new todo
		// em.getTransaction().begin();
		// Server server = new Server();
		// server.setRam(2);
		// em.persist(server);
		// em.getTransaction().commit();

		ServerDAO dao = new ServerDAO(em);
		ServerFacade serverFacade = new ServerFacadeImpl(dao);
//		// Server serverx = new Server();
//		// serverx.setID(1);
//		// serverFacade.save(serverx);
//		Server found = serverFacade.find(41);
//		System.out.println("Found: " + found);
//		// serverFacade.delete(found);
//		System.out.println("Found: " + found);
		List<Server> servers = serverFacade.findAll();
		for (Server s : servers) {
			for (VirtualMachine vm : s.getRunningVMs()) {
				System.out.println(vm.getID() + " ");
			}
		}
		
		System.out.println("------------------------------------");
		VirtualMachineDAO vmDAO = new VirtualMachineDAO(em);
		VirtualMachineFacade vmFacade = new VirtualMachineFacadeImpl(vmDAO);
		
		Server server = serverFacade.find(52);
		VirtualMachine vm = vmFacade.find(654);
		//vm.setHost(server);
		//VirtualMachine updatedVM = vmFacade.update(vm);
		System.out.println(vm.getHost().getID());
		
//		List<VirtualMachine> vms = vmFacade.findAll();
//		for (VirtualMachine v : vms) {
//			System.out.println(v.getHost().getID() + "  ");
//		}

		em.close();
	}
}
