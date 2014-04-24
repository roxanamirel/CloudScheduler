/**
 * 
 */
package database.facade;

import java.util.List;
import database.model.DataCenter;

/**
 * @author oneadmin
 *
 */
public interface DataCenterFacade {

	public abstract void save(DataCenter d);

	public abstract DataCenter update(DataCenter d);
	
	public abstract void delete(DataCenter d);

	public abstract DataCenter find(int entityID);

	public abstract List<DataCenter> findAll();
}
