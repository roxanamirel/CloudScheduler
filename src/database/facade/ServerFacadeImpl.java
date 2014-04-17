package database.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import database.dao.ServerDAO;
import database.model.Server;

public class ServerFacadeImpl implements ServerFacade {
	
	
	private ServerDAO serverDAO;
	
	public ServerFacadeImpl(ServerDAO dao){
		this.serverDAO = dao;
	}
	@Override
	public void save(Server server) {
		serverDAO.save(server);
	}

	@Override
	public Server update(Server server) {
		return serverDAO.update(server);
	}

	@Override
	public void delete(Server d) {
		serverDAO.delete(d.getId(), Server.class);
	}


	@Override
	public Server find(int entityID) {
		return serverDAO.find(entityID);
		
	}

	@Override
	public List<Server> findAll() {
		return serverDAO.findAll();
	}
}
