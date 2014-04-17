package database.facade;

import java.util.List;

import database.model.Server;

public interface ServerFacade {

	public abstract void save(Server d);

	public abstract Server update(Server d);
	
	public abstract void delete(Server d);

	public abstract Server find(int entityID);

	public abstract List<Server> findAll();
}
