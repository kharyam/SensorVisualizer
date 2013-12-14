package sensorvisualizer.model;

import org.apache.log4j.Logger;
import sensorvisualizer.controller.GmailSender;

/**
 * @author kharym
 *         <p/>
 *         This class will perform an alert action if a sensor's temperature goes beyond a certain threshold.
 *         It maintains the max temperature in memory but refreshes its associated sensor from the gateway
 *         server's status.
 */
public class TemperatureMonitor {

  private static String emailAddress;
  private Float maxTemperatureInfahrenheit;
  private Sensor sensor;
  private Logger log = Logger.getLogger(this.getClass());
  private boolean alertSent = false;

  /**
   * This variable indicates whether the sensor was updated or not during the last poll
   */
  private boolean upToDate = true;

  public TemperatureMonitor(Sensor sensor, Float maxTemp) {
    this.sensor = sensor;
    this.maxTemperatureInfahrenheit = maxTemp;
  }

  public Float getMaxTemperature() {
    return maxTemperatureInfahrenheit;
  }

  public void setMaxTemperature(Float maxTemperature) {
    this.maxTemperatureInfahrenheit = maxTemperature;
  }

  public Sensor getSensor() {
    return sensor;
  }

  public void setSensor(Sensor sensor) {

    this.sensor = sensor;
    Float temperature = getCurrentTempF();

    if (temperature != null && temperature > maxTemperatureInfahrenheit) {
      doAlert();
    } else {
      if (alertSent == true) {
        revokeAlert();
      }
    }

  }

  /**
   * Try to parse out the current temperature in Fahrenheit.  If it is in celsius, it will be converted.
   *
   * @return
   */
  public Float getCurrentTempF() {

    Float result = null;

    if (sensor != null && sensor.getCurrentReading() != null) {

      String currentReading = sensor.getCurrentReading();

      // Remove all special characters from the string (i.e, the degree symbol)
      currentReading = currentReading.replaceAll("[^\\x00-\\x7f]", "");

      boolean isfahrenheit = currentReading.contains("F");
      if (!isfahrenheit && !currentReading.contains("C")) {
        log.debug("No temperature specified in the current reading for sensor "
            + sensor);
        return null;
      }

      if (isfahrenheit) {
        int endIndex = currentReading.indexOf('F');
        int startIndex = (endIndex - 7 < 0) ? 0 : endIndex - 7;
        if (currentReading.contains("@")) {
          startIndex = currentReading.indexOf("@") + 1;
        }

        if (startIndex > endIndex) {
          return null;
        }

        if (endIndex > 0) {
          result = Float.parseFloat(currentReading.substring(
              startIndex, endIndex));
        }
      } else {
        int endIndex = currentReading.indexOf('C');
        int startIndex = (endIndex - 7 < 0) ? 0 : endIndex - 7;
        if (currentReading.contains("@")) {
          startIndex = currentReading.indexOf("@") + 1;
        }

        if (startIndex > endIndex) {
          return null;
        }

        if (endIndex > 0) {
          result = Float.parseFloat(currentReading.substring(
              startIndex, endIndex));

          // Convert Celsius to fahrenheit
          result = (result * (9 / 5)) + 32;
        }

      }

    } else {
      log.error("No current reading to get temperature from.");
    }

    return result;
  }

  public String getName() {

    String name = null;

    if (sensor != null) {
      name = sensor.getSensorName();
    } else {
      name = "Unknown";
    }

    return name;
  }

  public Integer getID() {

    Integer id = null;

    if (sensor != null) {
      id = sensor.getSensorID();
    } else {
      id = -1;
    }

    return id;
  }

  protected void doAlert() {
    if (!alertSent) {
      alertSent = true;
      log.warn("Sensor " + getName() + " has surpassed temperature threshold (" + getCurrentTempF() + " F)");
      log.warn("An email will be sent to " + getEmailAddress());
      GmailSender.sendGmail(getEmailAddress(), "Sensor Temperature High", "The temperature for sensor " + sensor.getSensorName() + " is " + getCurrentTempF() + "F, which is beyond the defined threshold of " + getMaxTemperature() + "F.");
    }
  }

  protected void revokeAlert() {
    if (alertSent) {
      alertSent = false;
      log.info("Sensor " + getName() + " is now below temperature threshold (" + getCurrentTempF() + " F)");
      log.info("An email will be sent to " + getEmailAddress());
      GmailSender.sendGmail(getEmailAddress(), "Sensor Temperature Normal", "The temperature for sensor " + sensor.getSensorName() + " is now normal, having dropped to " + getCurrentTempF() + "F, which is below the defined threshold of " + getMaxTemperature() + "F.");
    }
  }

  public static String getEmailAddress() {
    return emailAddress;
  }

  public static void setEmailAddress(String emailAddress) {

    TemperatureMonitor.emailAddress = emailAddress;
  }

  public boolean isUpToDate() {
    return upToDate;
  }

  public void setUpToDate(boolean upToDate) {
    this.upToDate = upToDate;
  }

  @Override
  public String toString() {

    return "Sensor: " + getName() + '(' + getID() + ')' + " Current temp: " + getCurrentTempF() + "F " + "Max Temp: " + getMaxTemperature();

  }

  public boolean isAlertSent() {
    return alertSent;
  }

  public void setAlertSent(boolean alertSent) {
    this.alertSent = alertSent;
  }

}
