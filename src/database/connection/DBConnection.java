package database.connection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBConnection {
	private static final String PERSISTENCE_UNIT_NAME = "todos";
	private static EntityManagerFactory factory;
	
	public static EntityManager connect(){
		 factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		 EntityManager em = factory.createEntityManager();
		 return em;
	}
}
