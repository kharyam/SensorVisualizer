package sensorvisualizer.model;

import org.apache.log4j.Logger;
import sensorvisualizer.controller.Controller;

import javax.swing.table.AbstractTableModel;

public class SensorTableModel extends AbstractTableModel {

  private static final long serialVersionUID = 1L;
  private Logger log = Logger.getLogger(this.getClass());

  // This model supports the 5 fields below but only the three relevent ones are displayed for now. Uncomment the next
  // line and comment out the following line to show all 3 fields in the table.
  //private String[] headers = {"Name","Current Temp(F)","Max Temp(F)","Network ID", "Application ID", "Status"};
  private String[] headers = {"Name", "Current Temp(F)", "Max Temp(F)"};

  public int getColumnCount() {
    return headers.length;
  }

  public int getRowCount() {
    return SensorStatuses.getInstance().getMonitors().size();
  }

  public Object getValueAt(int row, int col) {
    String result = null;

    switch (col) {
      case 0: {
        result = SensorStatuses.getInstance().getMonitors().get(row)
            .getName();
        break;
      }
      case 1: {
        Float currentTemp = SensorStatuses.getInstance().getMonitors()
            .get(row).getCurrentTempF();
        result = currentTemp == null ? "N/A" : SensorStatuses.getInstance()
            .getMonitors().get(row).getCurrentTempF().toString();
        break;
      }
      case 2: {
        result = SensorStatuses.getInstance().getMonitors().get(row)
            .getMaxTemperature().toString();
        break;
      }
      case 3: {
        result = "" + SensorStatuses.getInstance().getMonitors().get(row)
            .getSensor().getCsNetID();
      }
      case 4: {
        result = "" + SensorStatuses.getInstance().getMonitors().get(row)
            .getSensor().getApplicationID();
      }
      case 5: {
        result = "" + SensorStatuses.getInstance().getMonitors().get(row)
            .getSensor().getStatus();
      }

    }
    return result;
  }

  @Override
  public String getColumnName(int column) {

    return headers[column];

  }

  @Override
  public void setValueAt(Object value, int row, int column) {

    Float newMaxTemperature = null;

    try {
      newMaxTemperature = Float.parseFloat(value.toString());
    } catch (Throwable t) {
      log.info("Error parsing user entered max temp: " + value.toString(), t);
    }

    if (newMaxTemperature != null) {

      // Update the max temp in the monitor
      SensorStatuses.getInstance().getMonitors().get(row)
          .setMaxTemperature(newMaxTemperature);

      // Update the configuration value for this sensor's max temp
      Controller.getInstance().setConfigValue(SensorStatuses.SENSOR_PREFIX_PROPERTY + SensorStatuses.getInstance().getMonitors().get(row).getID(), newMaxTemperature.toString());

      // Refresh the latest values from the gateway now to check for a violation
      Controller.getInstance().updateTemperatures();
    }
  }

  @Override
  public boolean isCellEditable(int row, int column) {
    return (column == 2);
  }

}
