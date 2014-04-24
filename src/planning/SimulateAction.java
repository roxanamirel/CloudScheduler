package planning;

import java.util.Iterator;
import java.util.List;

import database.model.DataCenter;

public class SimulateAction{
	
	private DataCenter copyOfDataCenter;
	
	public SimulateAction(DataCenter dataCenter)
	{
		copyOfDataCenter = dataCenter;
	}
	
	public DataCenter executeActions( List<Action> act)
	{
		
		Iterator<Action> it = act.iterator();
		while(it.hasNext())
		{
			Action action = it.next();
			copyOfDataCenter = action.Do(copyOfDataCenter);
			
		}
		return copyOfDataCenter;
	}
	
	/** */
	public DataCenter undoActions( List<Action> act)
	{
		Iterator<Action> it = act.iterator();
		while(it.hasNext())
		{
			Action action = it.next();
			copyOfDataCenter = action.Undo(copyOfDataCenter);
			
		}
		return copyOfDataCenter;
	
	}
}
