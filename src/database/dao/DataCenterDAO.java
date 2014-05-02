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
		em.getTransaction().begin();
		DataCenter dataCenter =  em.merge(entity);
		em.getTransaction().commit();
		return dataCenter;
		
	}

	public DataCenter find(int entityID) {
		em.getTransaction().begin();
		DataCenter datacenter =  em.find(DataCenter.class, entityID);
		em.getTransaction().commit();
		return datacenter;
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<DataCenter> findAll() {
		em.getTransaction().begin();
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		cq.select(cq.from(DataCenter.class));
		List<DataCenter> list = em.createQuery(cq).getResultList();
		em.getTransaction().commit();
		return list;
	}
}
