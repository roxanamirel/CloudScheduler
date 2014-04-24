package database.facade;

import java.util.List;
import database.dao.DataCenterDAO;
import database.model.DataCenter;

public class DataCenterFacadeImpl implements DataCenterFacade {

	private DataCenterDAO dataCenterDAO;
	
	public DataCenterFacadeImpl(DataCenterDAO dataCenterDAO) {
		this.dataCenterDAO = dataCenterDAO;
	}
	
	@Override
	public void save(DataCenter d) {
		dataCenterDAO.save(d);		
	}

	@Override
	public DataCenter update(DataCenter d) {
		return dataCenterDAO.update(d);
	}

	@Override
	public void delete(DataCenter d) {
		dataCenterDAO.delete(d.getID(), DataCenter.class);		
	}

	@Override
	public DataCenter find(int entityID) {
		return dataCenterDAO.find(entityID);
	}

	@Override
	public List<DataCenter> findAll() {
		return dataCenterDAO.findAll();
	}
}
