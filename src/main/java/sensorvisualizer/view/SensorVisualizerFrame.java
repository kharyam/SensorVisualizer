package sensorvisualizer.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sensorvisualizer.controller.Controller;

public class SensorVisualizerFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField emailTF = new JTextField();
	private SensorPanel sensorPanel = new SensorPanel();
	
	public SensorVisualizerFrame() {
		super("Sensor Visualizer");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setLayout(new BorderLayout());
		
		JPanel emailPanel=new JPanel();
		emailPanel.setBorder(BorderFactory.createTitledBorder("Alert Destination"));
		emailPanel.setLayout(new GridLayout(1, 2));
		emailPanel.add(new JLabel("Email: "));
		emailPanel.add(emailTF);
		getContentPane().add(emailPanel,BorderLayout.SOUTH);
		
		getContentPane().add(sensorPanel,BorderLayout.CENTER);
		
		pack();
		setSize(700, 600);
		
		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		// Determine the new location of the window
		int w = getSize().width;
		int h = getSize().height;
		int x = (dim.width-w)/2;
		int y = (dim.height-h)/4;
		 
		// Move the window
		setLocation(x, y);

		
		WindowListener exitListener = new WindowAdapter() {

	        @Override
	        public void windowClosing(WindowEvent e) {
	            int confirm = JOptionPane.showOptionDialog(e.getComponent(),
	                    "Are you sure you would like to exit?",
	                    "Exit Confirmation", JOptionPane.YES_NO_OPTION,
	                    JOptionPane.QUESTION_MESSAGE, null, null, null);
	            if (confirm == JOptionPane.YES_OPTION) {
	                Controller.getInstance().exit();
	            }
	        }
	    };
	    
	    addWindowListener(exitListener);
		
	}

	public SensorPanel getSensorPanel() {
		return sensorPanel;
	}

	public void setSensorPanel(SensorPanel sensorPanel) {
		this.sensorPanel = sensorPanel;
	}

	public String getEmailAddress() {
		return emailTF.getText();
	}
	
	public void setEmailAddress(String address) {
		emailTF.setText(address);
	}
	
	

	
}
