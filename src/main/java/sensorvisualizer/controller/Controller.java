package sensorvisualizer.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.swing.Timer;

import org.apache.log4j.Logger;

import sensorvisualizer.model.SensorStatuses;
import sensorvisualizer.view.LoginFrame;
import sensorvisualizer.view.SensorVisualizerFrame;

/**
 * This is the application controller (MVC pattern).  It coordinates the activities between
 * the model (sensor data, see the classes in sensorvisualizer.model) and the view 
 * (user interface, see the classes in sensorvisualizer.view) 
 * 
 * @author kharym
 *
 */
public class Controller {

	
	private static Controller theInstance;
	private LoginFrame loginFrame;
	private SensorVisualizerFrame mainFrame;
	private Logger log = Logger.getLogger(this.getClass());
	private Timer timer;
	private Date nextUpdate;
	private int updateFrequency=10000;
	
	private File configFile;  // Config file, located in user home directory
	private Properties configProperties; // Config properties from config file
	public static final String EMAIL_ADDRESS_PROPERTY = "recipient.email.address";
	public static final String GMAIL_SENDER_ADDRESS_PROPERTY="gmail.sender.address";
	public static final String GMAIL_SENDER_PASSWORD_PROPERTY="gmail.sender.password";
	
	
	private Controller(){
		
		// Load the configuration file in the user home directory or create it
		initConfigFile(); 
		
		// Share the properties with the sensor statuses so that it can load the saved max temps from the config file
		SensorStatuses.getInstance().setConfigProperties(configProperties); 
	}

	public void init() {
		loginFrame = new LoginFrame();
		loginFrame.setVisible(true);
	}

	public static Controller getInstance() {
		if (theInstance == null) {
			theInstance = new Controller();
		}
		return theInstance;
	}

	public void login(String baseUri, String  username, char[] password) {

		loginFrame.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));

		boolean loginSuccess = SensorStatuses.getInstance().login(baseUri, username, new String(password),configProperties.getProperty(EMAIL_ADDRESS_PROPERTY));

		if (loginSuccess) {
			log.info("Login success for " + username);
			loginFrame.setVisible(false);
			mainFrame = new SensorVisualizerFrame();
			mainFrame.setEmailAddress(configProperties.getProperty(EMAIL_ADDRESS_PROPERTY));
			mainFrame.setVisible(true);
			timer = new Timer(100, new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					Date now = new Date();

					mainFrame.getSensorPanel().getProgressBar().setValue((int) (  (nextUpdate.getTime() - now.getTime())/(double)updateFrequency * 100.0) );

					if (now.getTime()>=nextUpdate.getTime()) {
						updateTemperatures();
						mainFrame.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
						nextUpdate.setTime(now.getTime() + updateFrequency);
					}

				}
			});

			nextUpdate = new Date();
			nextUpdate.setTime(nextUpdate.getTime()+updateFrequency);
			timer.start();

		} else {
			log.error("Login failed for " + username);
			loginFrame.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
		}

	}

	public void updateRefresh(int refreshInSeconds) {
		log.info("Updating refresh period to "+refreshInSeconds + " seconds.");
		updateTemperatures();
		updateFrequency=refreshInSeconds * 1000;
		nextUpdate.setTime(new Date().getTime() + updateFrequency);
	}

	public void updateTemperatures() {
		// Retrieve updates to the sensors in a new thread (SwingWorker) so that the GUI remains responsive
		SensorStatuses.getInstance().setNotificationEmail(mainFrame.getEmailAddress());
		setConfigValue(EMAIL_ADDRESS_PROPERTY,mainFrame.getEmailAddress()); // Store email address
		new StatusUpdater().execute();
	}

	public void repaint() {
		mainFrame.repaint();		
	}

	public String getConfigProperty(String key) {
		String result = null;
		return result;
	}

	private void initConfigFile(){

		configFile = new File(System.getProperty("user.home")+System.getProperty("file.separator")+"sensorconfig.properties");
		if (!configFile.exists()) {
			log.info("Creating configuration file: " + configFile.getAbsolutePath());
			try {
				boolean result = configFile.createNewFile();
				if (!result) {
					log.fatal("Failed to create config file");
				}
			} catch (IOException e) {
				log.fatal("Failed to create config file",e);
			} 

		}

		configProperties = new Properties();
		try {
			configProperties.load(new FileReader(configFile));
		} catch (Exception e) {
			log.fatal("Failed to read config file "+configFile.getAbsolutePath());
		}

	}
	
	public String getConfigValue(String key) {
		String result = "";
		if (configProperties != null) {
			result =  configProperties.getProperty(key);
		}
		return result;
	}
	
	public void setConfigValue(String key, String value) {
		if (configProperties != null) {
			configProperties.setProperty(key, value);
		}
	}
	
	public void saveConfigFile() {
		try {
			configProperties.store(new FileWriter(configFile), "Sensor Visualizer Configuration");
		} catch (IOException e) {
			log.error("Failed to save configuration file!");
		}

	}
	
	public void exit(){
		log.info("Saving config file.");
		saveConfigFile();
		
		log.info("Exiting");
		System.exit(0);
	}
	
}
