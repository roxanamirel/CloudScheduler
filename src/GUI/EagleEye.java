package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import util.ServerState;

import database.model.DataCenter;
import database.model.Server;
import database.model.VirtualMachine;

/**
 * The class EagleEye extends JPanel and overrides the paint() method to create
 * a graphical representation of the store. It contains several fields.
 * Basically, it does not have aware of what is being drawn. It draws the
 * simulation after a few parameters: number of waiting customers, number of
 * queues and number of clients in every queue. So the clients are always
 * ordered, the first one in always placed in the upper left corner. The queues
 * are placed according to the number of them, and for each client is added a
 * circle filled with a color: red for clients in queues and blue for waiting
 * clients. Besides this, there is also some other information sent and
 * displayed in order to follow easier the simulation: clients name for waiting
 * customers and service time for customers in queue
 * 
 * @author Antal Marcel
 * 
 */
public class EagleEye extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Array of VM
	 * */
	private ArrayList<VirtualMachine> allVMs = new ArrayList<VirtualMachine>();
	/**
	 * Array of servers
	 * */
	private ArrayList<Server> allServers = new ArrayList<Server>();

	/**
	 * number of running servers
	 */
	private int nrServers = 0;
	/**
	 * array storing each servers size by CPU
	 * */
	private ArrayList<Float> serverSize = new ArrayList<Float>();
	/**
	 * Maximum size of a server
	 * */
	private float maxServer = 0;
	/**
	 * array memorizing the number of VMs in each server
	 */
	private int[] queue = new int[] { 5, 7, 8 };

	/**
	 * queue width in pixels
	 */
	private int queueWidth = 30;// queue width in pixels

	private ArrayList<Point> points = new ArrayList<Point>();

	private int radiusVM = 0;

	private DataCenter dc = null;

	/**
	 * constructor
	 * 
	 * @param maxQueues
	 *            maximum queues allowed
	 */
	public EagleEye() {
		this.setBackground(Color.WHITE);
		this.addMouseListener(this);
		this.setLayout(null);
		;

	}

	public void updateGraphics(List<VirtualMachine> newVMs,
			List<Server> newServers) {
		allVMs.clear();
		allVMs.addAll(newVMs);
		allServers.clear();
		allServers.addAll(newServers);

		nrServers = allServers.size();

		System.out.println(nrServers);

		float max = 0;
		serverSize.clear();
		Iterator<Server> it = allServers.iterator();
		ArrayList<Float> serverSize1 = new ArrayList<Float>();
		queue = new int[nrServers];
		int i = 0;
		while (it.hasNext()) {
			Server saux = it.next();
			queue[i] = saux.getRunningVMs().size();
			i++;
			float aux = saux.getCPU().getTotalFrequency();
			serverSize1.add(aux);
			if (aux > max)
				max = aux;
		}
		maxServer = max;
		System.out.println(serverSize1.size());
		// normalize sizes. Maximum size is 100
		for (i = 0; i < serverSize1.size(); i++) {
			float aux = serverSize1.get(i) / maxServer * 100;
			serverSize.add(aux);
		}

		this.repaint();
		this.validate();
	}

	/**
	 * paint method
	 */
	@Override
	public void paint(Graphics g) {

		Color cyan = new Color(0, 102, 204);
		// Color

		points.clear();

		Graphics2D g2 = (Graphics2D) g;
		Dimension d = getSize();

		Font f = new Font("SansSerif", Font.PLAIN, 12);
		FontMetrics fm = g2.getFontMetrics(f);
		Font fBold = new Font("SansSerif", Font.BOLD, 14);
		g2.setFont(f);

		int dx = d.width;
		int dy = d.height;
		Color fg = Color.WHITE;
		g2.setPaint(fg);
		g2.fill(new Rectangle2D.Double(0, 0, d.width, d.height));
		g2.setPaint(Color.BLACK);
		g2.draw(new Line2D.Double(d.width / 4, 0, d.width / 4, d.height));

		g2.setFont(fBold);
		String s2 = "Pending Virtual";
		g2.setColor(Color.BLACK);
		g2.drawString(s2, 5, 15);
		s2 = "     Machines";
		g2.drawString(s2, 5, 30);
		// draw queues
		int dq = dy / (2 * nrServers + 1);
		int xx = dx / 3 - queueWidth;
		int yy = queueWidth;

		TreeSet<VirtualMachine> deployed = new TreeSet<VirtualMachine>();

		for (int i = 0; i < serverSize.size(); i++) {
			float size = serverSize.get(i);
			g2.draw(new Rectangle2D.Double(xx, yy, 2 * dx / 3 * size / 100,
					queueWidth));
			g2.setFont(fBold);
			String ss = "Server " + allServers.get(i).getID();
			int width1 = fm.stringWidth(ss);// get string width

			g2.drawString(ss, (int) (xx), yy - 5);

			if (allServers.get(i).getState().equals("OFF")) {
				g2.setColor(Color.RED);
			} else if (ServerState.valueOf(allServers.get(i).getState()) == ServerState.ON) {
				g2.setColor(Color.GREEN);
			}

			g2.fillOval(xx + width1 + 30, yy - 17, 15, 15);
			g2.setColor(Color.BLACK);
			if (allServers.get(i).getState().equals("OFF")) {
				// g2.setColor(cyan);
				// g2.fillRect(xx, yy, (int) (2 * dx / 3 * size / 100),
				// queueWidth);
				ss = "Server OFF";
				width1 = fm.stringWidth(ss);// get string width
				// g2.setColor(Color.WHITE);
				g2.drawString(ss,
						(int) (xx + dx / 3 * size / 100 - width1 / 2), yy + 20);
			}
			yy = yy + dq;
		}

		g2.setFont(f);
		xx = dx / 3 - queueWidth;
		yy = queueWidth + queueWidth / 6;
		int radius = queueWidth / 3;
		int d2 = 2 * radius;
		g2.setPaint(Color.RED);
		radiusVM = radius;

		// draw running VMS
		for (int i = 0; i < nrServers; i++) {
			xx = dx / 3 - queueWidth;// -2*queueWidth;
			g2.setPaint(Color.RED);
			List<VirtualMachine> localVMs = allServers.get(i).getRunningVMs();
			for (int j = 0; j < localVMs.size(); j++) {
				g2.setPaint(Color.RED);
				g2.drawOval(xx, yy, 2 * d2, d2);
				g2.fillOval(xx, yy, 2 * d2, d2);
				Point pp = new Point();
				pp.x = xx + 2 * radius;
				pp.y = yy + radius;
				pp.VM = localVMs.get(j);
				points.add(pp);
				deployed.add(localVMs.get(j));
				// if we have information about the customer, write them

				g2.setPaint(Color.BLACK);
				String ss = localVMs.get(j).getID() + "";
				int width1 = fm.stringWidth(ss);// get string width
				g2.drawString(ss, (int) (xx + 2 * radius - width1 / 2), yy
						+ radius + 5);

				xx = xx + 2 * d2;
			}
			yy = yy + dq;
		}

		// draw pending VMs
		xx = 10;
		yy = 40;

		g2.setFont(f);
		g2.setPaint(cyan);
		for (int i = 0; i < allVMs.size(); i++) {

			// write information about them
			VirtualMachine aux = allVMs.get(i);
			if (!deployed.contains(aux)) {
				// if(aux.getState().equals("UNDEPLOYED")){
				g2.setPaint(cyan);
				g2.drawOval(xx, yy, 2 * d2, d2);
				g2.fillOval(xx, yy, 2 * d2, d2);
				Point pp = new Point();
				pp.x = xx + radius;
				pp.y = yy + radius;
				pp.VM = aux;
				points.add(pp);
				g2.setPaint(Color.WHITE);
				String ss = aux.getID() + "";
				int width1 = fm.stringWidth(ss);// get string width
				g2.drawString(ss, (int) (xx + 2 * radius - width1 / 2), yy
						+ radius + 5);

				xx = xx + 2 * d2 + radius;
				if (xx > dx / 4 - d2) {
					xx = 10;
					yy = yy + d2 + radius;
				}
			}

		}

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		int x = arg0.getX();
		int y = arg0.getY();
		for (int i = 0; i < points.size(); i++) {
			Point paux = points.get(i);
			double distx = x - paux.x;
			double disty = y - paux.y;
			if (Math.sqrt(distx * distx + disty * disty) < radiusVM) {
				System.out.println(x + "  " + y + " " + paux.VM.getID());
				MigrationFrame frame = new MigrationFrame(paux.VM, dc);
				frame.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {

					}
				});
				frame.setSize(600, 200);
				frame.setTitle("Migrate Virtual Machine");
				// frame.pack();
				frame.setVisible(true);
				frame.repaint();
				frame.validate();
			}
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void setDataCenter(DataCenter datacenter) {
		this.dc = datacenter;
	}

}
