package database.connection;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import database.dao.ServerDAO;
import database.facade.ServerFacade;
import database.facade.ServerFacadeImpl;
import database.model.Server;



public class Main {
     
	  public static void main(String[] args) {
	  
	    EntityManager em = DBConnection.connect();
	    // read the existing entries and write to console
//	    Query q = em.createQuery("select t from Server t");
//	    List<Server> todoList = q.getResultList();
//	    for (Server todo : todoList) {
//	      System.out.println(todo);
//	    }
//	    System.out.println("Size: " + todoList.size());

	    // create new todo
//	    em.getTransaction().begin();
//	    Server server = new Server();
//	    server.setRam(2);
//	    em.persist(server);
//	    em.getTransaction().commit();
	  
	    
	    ServerDAO dao = new ServerDAO(em);
	    ServerFacade serverFacade = new ServerFacadeImpl(dao);
//	    Server serverx = new Server();
//	    serverx.setRam(34885);
//	    serverFacade.save(serverx);
//	    Server found = serverFacade.find(21);
//	    System.out.println("Found: "  + found);
//	   // serverFacade.delete(found);
//	    System.out.println("Found: "  + found);
	   
	    List<Server> servers = serverFacade.findAll();
	    for(Server s:servers){
	    	System.out.println(s.getId()+ "  "  + s.getRam());
	    }
	    em.close();
	  }
}
