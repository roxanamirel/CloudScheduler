package userinterface;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import planning.actions.Action;
import planning.actions.InterCloudMigration;
import tcp.TCPServer;
import logger.CloudLogger;
import monitoring.util.FacadeFactory;
import database.facade.VirtualMachineFacade;
import database.model.DataCenter;
import database.model.Server;
import database.model.VirtualMachine;
import execution.Execution;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CloudManagerGui {

	private static CloudManagerGui instance = null;
	private JFrame frame;
	private JPanel serverPanel;
	private JButton interCloudButton;
	private JRadioButton dataCenterRadioButtonSelected;
	private JRadioButton vmRadioButtonSelected;
	private DataCenter datacenter;
	public static CloudManagerGui getInstance() {
		if (instance == null) {
			instance = new CloudManagerGui();
		}
		return instance;
	}

	private CloudManagerGui() {

		TCPServer server = new TCPServer();
		Thread t = new Thread(server);
		t.start();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 492, 343);
		frame.getContentPane().setLayout(new BorderLayout());

		Dimension prefDimension = new Dimension(100, 30);

		Container pane = frame.getContentPane();
		JLabel emptyLabel = new JLabel("");
		emptyLabel.setPreferredSize(prefDimension);

		serverPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
		serverPanel.setBackground(Color.WHITE);

		pane.add(serverPanel, BorderLayout.WEST);

		JPanel dataCenterPanel = new JPanel(new GridLayout(4, 1));
		ButtonGroup dataCenterRadioButtons = new ButtonGroup();
		for (int i = 0; i < 4; i++) {
			JRadioButton jRadioButton = new JRadioButton("Datacenter " + i);
			jRadioButton.addActionListener(new DataCenterRadioButtonListener());
			dataCenterRadioButtons.add(jRadioButton);
			dataCenterPanel.add(jRadioButton);
		}

		pane.add(dataCenterPanel, BorderLayout.EAST);

		pane.add(emptyLabel, BorderLayout.SOUTH);
		pane.add(emptyLabel, BorderLayout.NORTH);

		interCloudButton = new JButton();
		interCloudButton.setText("<html>Inter-Cloud<br />Migrate</html>");
		interCloudButton.addActionListener(new InterCloudListener());

		JPanel centerArea = new JPanel(new GridLayout(3, 1));
		centerArea.add(new JLabel());
		centerArea.add(interCloudButton);
		centerArea.add(new JLabel());

		pane.add(centerArea, BorderLayout.CENTER);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * Launch the application.
	 */
	public void update(DataCenter dataCenter) {
		this.datacenter = dataCenter;
		try {
			updateServerSide(dataCenter.getServerPool());
			instance.frame.setVisible(true);
		} catch (Exception e) {
			CloudLogger.getInstance().LogInfo(e.getMessage());
		}
	}

	private void updateServerSide(List<Server> serverPool) {

		instance.serverPanel.removeAll();

		List<JPanel> serverJPanels = new ArrayList<JPanel>();
		ButtonGroup vmGroup = new ButtonGroup();

		for (Server server : serverPool) {
			JPanel serverPanel = new JPanel();
			serverPanel.setLayout(new GridLayout(
					server.getRunningVMs().size() + 1, 1));
			serverPanel.setBackground(Color.lightGray);
			
			JPanel vmPanel = new JPanel();
			vmPanel.setLayout(new GridLayout(
					server.getRunningVMs().size() + 1, 1));
			vmPanel.setBackground(Color.GREEN);			
			for (VirtualMachine virtualMachine : server.getRunningVMs()) {
				JRadioButton vmRadioButton = new JRadioButton(""
						+ virtualMachine.getID());
				vmRadioButton.addActionListener(new VMRadioButtonListener());				
				vmGroup.add(vmRadioButton);				
				vmPanel.add(vmRadioButton);
			}
			serverPanel.add(vmPanel);
			serverPanel.add(new JLabel("Server: " + server.getID()));
			serverJPanels.add(serverPanel);
		}

		for (JPanel jPanel : serverJPanels) {
			instance.serverPanel.add(jPanel);
		}
	}

	private class InterCloudListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			JOptionPane.showMessageDialog(frame, "You choosed to migrate to "
					+ dataCenterRadioButtonSelected.getText()
					+ "\n the virtual machine with identification: "
					+ vmRadioButtonSelected.getText());
			VirtualMachineFacade vmFacade = new FacadeFactory()
					.createVirtualMachineFacade();
			VirtualMachine vm = vmFacade.find(Integer
					.parseInt(vmRadioButtonSelected.getText()));
			Action action = new InterCloudMigration(null, null, vm);
			Execution execution = new Execution();
			List<Action> interCloudActions = new ArrayList<Action>();
			interCloudActions.add(action);
			execution.updateDatabase(datacenter, interCloudActions);
			execution.executeActions(interCloudActions);
			CloudLogger.getInstance().LogInfo(
					"--------------------------------------------------");
			CloudLogger.getInstance().LogInfo(
					action.toString() + " will be executed ...");
			
			

		}
	}

	private class DataCenterRadioButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dataCenterRadioButtonSelected = (JRadioButton) e.getSource();
		}
	}

	private class VMRadioButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			vmRadioButtonSelected = (JRadioButton) e.getSource();
		}
	}
}
