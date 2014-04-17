package database.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;

import database.model.Server;

public class ServerDAO  {
	public ServerDAO(){}
	private static final String PERSISTENCE_UNIT_NAME = "todos";
	private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = factory.createEntityManager();
	
	public void save(Server entity) {
		em.persist(entity);
	}

	protected void delete(Object id, Class<Server> classe) {
		Server entityToBeRemoved = em.getReference(classe, id);

		em.remove(entityToBeRemoved);
	}

	public Server update(Server entity) {
		return em.merge(entity);
	}

//	public Server find(int entityID) {
//		return em.find(Class<Server>,entityID);
//	}

	// Using the unchecked because JPA does not have a
	// em.getCriteriaBuilder().createQuery()<Server> method
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public List<Server> findAll() {
//		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
//		cq.select(cq.from(entityClass));
//		return em.createQuery(cq).getResultList();
//	}

}
