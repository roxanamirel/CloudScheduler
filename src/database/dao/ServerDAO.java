package database.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import database.model.Server;

public class ServerDAO {

	private EntityManager em;

	public ServerDAO(EntityManager em) {
		this.em = em;
	}

	public void save(Server entity) {
		em.getTransaction().begin();
		em.persist(entity);
		em.getTransaction().commit();
	}

	public void delete(Object id, Class<Server> classe) {
		em.getTransaction().begin();
		Server entityToBeRemoved = em.getReference(classe, id);
		em.remove(entityToBeRemoved);
		em.getTransaction().commit();
	}

	public Server update(Server entity) {
		return em.merge(entity);
		
	}

	public Server find(int entityID) {
		return em.find(Server.class, entityID);
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Server> findAll() {
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		cq.select(cq.from(Server.class));
		return em.createQuery(cq).getResultList();
	}

}
