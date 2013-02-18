package sensorvisualizer.controller;

import javax.swing.SwingWorker;

import sensorvisualizer.model.SensorStatuses;

/**
 * This class will retrieve the latest status of the sensors in a separate thread
 * 
 * @author kharym
 *
 */
public class StatusUpdater extends SwingWorker<String, Object> {
	@Override
	public String doInBackground() {
		SensorStatuses.getInstance().updateTemperatures();
		return "done";
	}

	@Override
	protected void done() {
		try {
			Controller.getInstance().repaint();
		} catch (Exception ignore) {
		}
	}
}