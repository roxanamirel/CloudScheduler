package GUI;

import java.util.List;

import database.model.DataCenter;
import database.model.Server;
import database.model.VirtualMachine;



public interface GUICommands {
	
	public void updateGraphics(DataCenter datacenter);
	
	public void updateServers(List<Server> servers, List<VirtualMachine> vms) ;
	
	public void printlnText(String string);

}
