package sensorvisualizer.model;

/**
 * Simple bean representing a sensor and all its available data
 *
 * @author kharym
 */
public class Sensor {

  private String sensorName;
  private int sensorID;
  private int applicationID;
  private int csNetID;
  private String lastCommunicationDate; // TODO Change this to a java.util.Date
  private String nextCommunicationDate; // TODO Change this to a java.util.Date
  private int lastDataMessageID;
  private int powerSourceID;
  private int Status;
  private String canUpdate; // TODO make this a boolean
  private String currentReading;
  private int batteryLevel;
  private int signalStrength;
  private String alertsActive; // TODO make this a boolean

  public Sensor(String sensorName, int sensorID, int applicationID,
                int csNetID, String lastCommunicationDate,
                String nextCommunicationDate, int lastDataMessageID,
                int powerSourceID, int status, String canUpdate,
                String currentReading, int batteryLevel, int signalStrength,
                String alertsActive) {
    super();
    this.sensorName = sensorName;
    this.sensorID = sensorID;
    this.applicationID = applicationID;
    this.csNetID = csNetID;
    this.lastCommunicationDate = lastCommunicationDate;
    this.nextCommunicationDate = nextCommunicationDate;
    this.lastDataMessageID = lastDataMessageID;
    this.powerSourceID = powerSourceID;
    Status = status;
    this.canUpdate = canUpdate;
    this.currentReading = currentReading;
    this.batteryLevel = batteryLevel;
    this.signalStrength = signalStrength;
    this.alertsActive = alertsActive;
  }

  @Override
  public String toString() {
    return "Sensor [sensorName=" + sensorName + ", sensorID="
        + sensorID + ", applicationID=" + applicationID + ", csNetID="
        + csNetID + ", lastCommunicationDate=" + lastCommunicationDate
        + ", nextCommunicationDate=" + nextCommunicationDate
        + ", lastDataMessageID=" + lastDataMessageID
        + ", powerSourceID=" + powerSourceID + ", Status=" + Status
        + ", canUpdate=" + canUpdate + ", currentReading="
        + currentReading + ", batteryLevel=" + batteryLevel
        + ", signalStrength=" + signalStrength + ", alertsActive="
        + alertsActive + "]";
  }

  public String getSensorName() {
    return sensorName;
  }

  public void setSensorName(String sensorName) {
    this.sensorName = sensorName;
  }

  public int getSensorID() {
    return sensorID;
  }

  public void setSensorID(int sensorID) {
    this.sensorID = sensorID;
  }

  public int getApplicationID() {
    return applicationID;
  }

  public void setApplicationID(int applicationID) {
    this.applicationID = applicationID;
  }

  public int getCsNetID() {
    return csNetID;
  }

  public void setCsNetID(int csNetID) {
    this.csNetID = csNetID;
  }

  public String getLastCommunicationDate() {
    return lastCommunicationDate;
  }

  public void setLastCommunicationDate(String lastCommunicationDate) {
    this.lastCommunicationDate = lastCommunicationDate;
  }

  public String getNextCommunicationDate() {
    return nextCommunicationDate;
  }

  public void setNextCommunicationDate(String nextCommunicationDate) {
    this.nextCommunicationDate = nextCommunicationDate;
  }

  public int getLastDataMessageID() {
    return lastDataMessageID;
  }

  public void setLastDataMessageID(int lastDataMessageID) {
    this.lastDataMessageID = lastDataMessageID;
  }

  public int getPowerSourceID() {
    return powerSourceID;
  }

  public void setPowerSourceID(int powerSourceID) {
    this.powerSourceID = powerSourceID;
  }

  public int getStatus() {
    return Status;
  }

  public void setStatus(int status) {
    Status = status;
  }

  public String getCanUpdate() {
    return canUpdate;
  }

  public void setCanUpdate(String canUpdate) {
    this.canUpdate = canUpdate;
  }

  public String getCurrentReading() {
    return currentReading;
  }

  public void setCurrentReading(String currentReading) {
    this.currentReading = currentReading;
  }

  public int getBatteryLevel() {
    return batteryLevel;
  }

  public void setBatteryLevel(int batteryLevel) {
    this.batteryLevel = batteryLevel;
  }

  public int getSignalStrength() {
    return signalStrength;
  }

  public void setSignalStrength(int signalStrength) {
    this.signalStrength = signalStrength;
  }

  public String getAlertsActive() {
    return alertsActive;
  }

  public void setAlertsActive(String alertsActive) {
    this.alertsActive = alertsActive;
  }

}
