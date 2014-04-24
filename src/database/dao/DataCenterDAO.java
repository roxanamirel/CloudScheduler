package database.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import database.model.DataCenter;

public class DataCenterDAO {

	private EntityManager em;

	public DataCenterDAO(EntityManager em) {
		this.em = em;
	}

	public void save(DataCenter entity) {
		em.getTransaction().begin();
		em.persist(entity);
		em.getTransaction().commit();
	}

	public void delete(Object id, Class<DataCenter> classe) {
		em.getTransaction().begin();
		DataCenter entityToBeRemoved = em.getReference(classe, id);
		em.remove(entityToBeRemoved);
		em.getTransaction().commit();
	}

	public DataCenter update(DataCenter entity) {
		return em.merge(entity);
		
	}

	public DataCenter find(int entityID) {
		return em.find(DataCenter.class, entityID);
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<DataCenter> findAll() {
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		cq.select(cq.from(DataCenter.class));
		return em.createQuery(cq).getResultList();
	}
}
