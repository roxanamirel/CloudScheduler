package GUI;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import planning.actions.Action;
import planning.actions.InterCloudMigration;

import logger.CloudLogger;
import monitoring.util.FacadeFactory;

import database.facade.VirtualMachineFacade;
import database.model.DataCenter;
import database.model.VirtualMachine;
import execution.Execution;

public class MigrationFrame extends JFrame implements ActionListener{

	private JPanel pane=new JPanel(new GridBagLayout());
	
	private JLabel label = new JLabel();
	private VirtualMachine VM = new VirtualMachine();
	private JComboBox<String> combo = new JComboBox<String>();
	private JButton ok = new JButton("OK");
	private JButton cancel = new JButton("Cancel");
	private DataCenter datacenter;
	
	
	public MigrationFrame(VirtualMachine VM, DataCenter dc){
		this.datacenter = dc;
		this.VM = VM;
		label.setText("Migrate Virtual Machine with ID "+VM.getID()+" to datacenter ");
		pane.setLayout(new GridBagLayout());
    	GridBagConstraints c=new GridBagConstraints();
    	combo.addItem("DataCenter 1");
    	combo.addItem("DataCenter 2");
    	c.gridx = 0;
    	c.gridy = 0;
    	c.gridwidth = 3;
    	pane.add(label,c);
    	
    	c.gridwidth = 1;
    	c.gridx = 3;
    	pane.add(combo,c);
    	
    	c.gridwidth = 1;
    	c.gridx = 1;
    	c.gridy = 1;

    	pane.add(ok,c);
    	
    	c.gridx = 2;
    	pane.add(cancel,c);
    	
    	cancel.addActionListener(this);
    	ok.addActionListener(this);
    	
    	this.add(pane);
    	
        this.addWindowListener(new WindowAdapter() {
                  public void windowClosing(WindowEvent e) {
                	 
                  }
              });
          this.setTitle("MIGRATION");
          pane.setBackground(Color.WHITE);
          this.setBackground(Color.WHITE);
          this.pack();
          this.setVisible(true);
       
        
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if(source == ok){
			//migrate
			
			
			VirtualMachine vm = this.VM;
			Action action = new InterCloudMigration(null, null, vm);
			Execution execution = new Execution();
			List<Action> interCloudActions = new ArrayList<Action>();
			interCloudActions.add(action);
			execution.updateDatabase(datacenter, interCloudActions);
			execution.executeActions(interCloudActions);
			DataCenterInterface.getInstance().printlnText("--------------------------------------------------");
			DataCenterInterface.getInstance().printlnText(action.toString() + " will be executed ...");
			CloudLogger.getInstance().LogInfo(
					"--------------------------------------------------");
			CloudLogger.getInstance().LogInfo(
					action.toString() + " will be executed ...");
			setVisible(false); //you can't see me!
			dispose(); //Destroy the JFrame object
		}
		if(source == cancel){
			setVisible(false); //you can't see me!
			dispose(); //Destroy the JFrame object
		}
		
	}

}
