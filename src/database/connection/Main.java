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
	private static final String PERSISTENCE_UNIT_NAME = "todos";
	  private static EntityManagerFactory factory;
     
	  public static void main(String[] args) {
	    factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	    EntityManager em = factory.createEntityManager();
	    // read the existing entries and write to console
	    Query q = em.createQuery("select t from Server t");
	    List<Server> todoList = q.getResultList();
	    for (Server todo : todoList) {
	      System.out.println(todo);
	    }
	    System.out.println("Size: " + todoList.size());

	    // create new todo
	    em.getTransaction().begin();
	    Server server = new Server();
	    server.setRam(2);
	    em.persist(server);
	    em.getTransaction().commit();
	    em.close();
	    ServerDAO dao = new ServerDAO();
	    ServerFacade serverFacade = new ServerFacadeImpl(dao);
	    Server serverx = new Server();
	    serverx.setRam(90);
	    Server s = serverFacade.update(serverx);
	    System.out.println("Hello server "+ s.getRam());
	  }
}
