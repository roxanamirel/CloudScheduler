package database.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import database.model.VirtualMachine;

public class VirtualMachineDAO  {
	private EntityManager em;
	
	public VirtualMachineDAO(EntityManager em) {
		this.em = em;
	}
	public void save(VirtualMachine entity) {
		em.getTransaction().begin();
		em.persist(entity);
		em.getTransaction().commit();
	}

	public void delete(Object id, Class<VirtualMachine> classe) {
		em.getTransaction().begin();
		VirtualMachine entityToBeRemoved = em.getReference(classe, id);
		em.remove(entityToBeRemoved);
		em.getTransaction().commit();
	}

	public VirtualMachine update(VirtualMachine entity) {
		em.getTransaction().begin();
		VirtualMachine vm = em.merge(entity);
		em.getTransaction().commit();
		return vm;
	}

	public VirtualMachine find(int entityID) {
		return em.find(VirtualMachine.class, entityID);
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<VirtualMachine> findAll() {
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		cq.select(cq.from(VirtualMachine.class));
		return em.createQuery(cq).getResultList();
	}
}
