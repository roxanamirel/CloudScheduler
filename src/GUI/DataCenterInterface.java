package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import tcp.TCPServer;

import database.model.*;

public class DataCenterInterface extends JFrame implements GUICommands,
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5781690011442351460L;

	private static DataCenterInterface instance;

	public static DataCenterInterface getInstance() {
		if (instance == null) {
			instance = new DataCenterInterface();
		}
		return instance;
	}

	private JPanel pane = new JPanel(new GridBagLayout());
	/**
	 * object for graphical representation
	 */
	private EagleEye CCTV;

	private JLabel showTime = new JLabel("00:00");

	/**
	 * text area to display a log of events
	 */
	private TextArea info = new TextArea("", 20, 60, TextArea.SCROLLBARS_BOTH);

	/**
	 * timer
	 */
	private Timer timer;
	/**
	 * current simulation time
	 */
	private int timeSec = 0;

	private DataCenterInterface() {

		TCPServer server = new TCPServer();
		Thread t = new Thread(server);
		t.start();
		Font f = new Font("TimesNewRoman", Font.BOLD, 14);
		info.setFont(f);
		info.setEditable(false);
		info.setBackground(new Color(28, 9, 44));
		info.setForeground(Color.WHITE);
		info.append("> Algoritm started \n");
		CCTV = new EagleEye();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		pane.add(showTime);
		c.gridy = 1;
		c.ipadx = 500;
		c.ipady = 500;
		pane.add(CCTV, c);
		c.gridy = 2;
		c.ipadx = 1;
		c.ipady = 1;
		pane.add(info, c);
		this.add(pane);
		this.setSize(1200, 1600);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

			}
		});
		this.setTitle("CLOUD SCHEDULER");

		pane.setBackground(Color.WHITE);
		this.setBackground(Color.WHITE);
		this.pack();
		this.setVisible(true);

		timer = new Timer(1000, this);
		timer.setDelay(1000);
		timer.start();
	}

	public void display() {
		DataCenterInterface frame = DataCenterInterface.getInstance();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setTitle("CLOUD SCHEDULER");
		frame.pack();
		frame.setVisible(true);
		frame.repaint();
		frame.validate();
	}

	public void updateGraphics(DataCenter datacenter) {
		List<Server> servers = datacenter.getServerPool();
		List<VirtualMachine> VMs = datacenter.getVMPool();
		CCTV.setDataCenter(datacenter);
		CCTV.updateGraphics(VMs, servers);
	}

	@Override
	public void updateServers(List<Server> servers, List<VirtualMachine> vms) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if (source == timer) {
			timeSec++;
			showTime.setText(timeSec / 60 + " : " + timeSec % 60);
		}

	}

	@Override
	public void printlnText(String string) {
		// TODO Auto-generated method stub
		info.append("> " + string + "\n");

	}

}
