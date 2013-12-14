package sensorvisualizer.model;

import sensorvisualizer.client.SensorRestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * This class maintains the status of all sensors.
 *
 * @author kharym
 */
public class SensorStatuses {

  /**
   * Prefix for the sensor property in the configuration file
   */
  public static final String SENSOR_PREFIX_PROPERTY = "sensor.";

  private static SensorStatuses theInstance = null;

  private List<TemperatureMonitor> monitors;
  private SensorRestClient client;

  private String name;
  private Integer networkID;
  private Integer applicationID;
  private Integer status;
  private static final float DEFAULT_MAX_FLOAT = 100f;

  /**
   * A mapping from sensor identifier to maximum temperature
   */
  private Properties configProperties = null;

  public static SensorStatuses getInstance() {
    if (theInstance == null) {
      theInstance = new SensorStatuses();
    }

    return theInstance;

  }

  private SensorStatuses() {
    client = SensorRestClient.getInstance();
  }

  public boolean login(String baseUri, String username, String password, String notifEmail) {
    boolean success = client.login(baseUri, username, password);
    if (success) {
      setNotificationEmail(notifEmail);
      populateMonitors();
      updateTemperatures();
    }
    return success;
  }

  public void setFilterCriteria(String name, Integer networkID,
                                Integer applicationID, Integer status) {

    this.name = name;
    this.networkID = networkID;
    this.applicationID = applicationID;
    this.status = status;

  }

  public void updateTemperatures() {

    // Set the status of all monitors to out of date
    for (TemperatureMonitor monitor : monitors) {
      monitor.setUpToDate(false);
    }

    List<Sensor> sensors = client.getSensorList(name, networkID,
        applicationID, status);

    for (Sensor sensor : sensors) {
      updateMonitor(sensor);
    }
  }

  private void updateMonitor(Sensor sensor) {
    boolean updated = false;
    for (TemperatureMonitor monitor : monitors) {
      if (monitor.getID().equals(sensor.getSensorID())) {
        monitor.setSensor(sensor);
        monitor.setUpToDate(true);
        updated = true;
        break;
      }
    }

    // If an update wasn't made, that means this is a new sensor that didn't
    // exist previously, add it
    if (!updated) {
      monitors.add(new TemperatureMonitor(sensor, getMaxTemp(sensor.getSensorID())));
    }

  }

  private void populateMonitors() {

    monitors = new ArrayList<TemperatureMonitor>();
    List<Sensor> sensors = client.getSensorList(name, networkID,
        applicationID, status);

    for (Sensor sensor : sensors) {
      monitors.add(new TemperatureMonitor(sensor, getMaxTemp(sensor.getSensorID())));
    }

  }

  private Float getMaxTemp(int id) {

    Float result = DEFAULT_MAX_FLOAT;

    if (configProperties != null && configProperties.get(SENSOR_PREFIX_PROPERTY + id) != null) {
      Float mappedTemp = Float.parseFloat(configProperties.get(SENSOR_PREFIX_PROPERTY + id).toString());
      if (mappedTemp != null) {
        result = mappedTemp;
      }
    }

    return result;
  }

  public List<TemperatureMonitor> getMonitors() {
    return monitors;
  }

  public void setNotificationEmail(String email) {
    TemperatureMonitor.setEmailAddress(email);
  }

  public void setConfigProperties(Properties properties) {
    this.configProperties = properties;
  }

}
