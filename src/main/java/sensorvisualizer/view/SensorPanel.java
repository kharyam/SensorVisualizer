package sensorvisualizer.view;

import sensorvisualizer.controller.Controller;
import sensorvisualizer.model.SensorTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

public class SensorPanel extends JPanel {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private JTextField nameTF = new JTextField();
  private JFormattedTextField networkIdTF = new JFormattedTextField(
      NumberFormat.getInstance());
  private JFormattedTextField applicationIdTF = new JFormattedTextField(
      NumberFormat.getInstance());
  private JFormattedTextField statusTF = new JFormattedTextField(
      NumberFormat.getInstance());
  private JTable sensorTable;
  private JComboBox<String> refreshCB;
  private JProgressBar progressBar = new JProgressBar(0, 100);

  public SensorPanel() {
    setLayout(new BorderLayout());

    JPanel filterPanel = new JPanel();
    filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));
    filterPanel.setLayout(new GridLayout(4, 2));
    filterPanel.add(new JLabel("Name:"));
    filterPanel.add(nameTF);
    filterPanel.add(new JLabel("Network ID:"));
    filterPanel.add(networkIdTF);
    filterPanel.add(new JLabel("Application ID:"));
    filterPanel.add(applicationIdTF);
    filterPanel.add(new JLabel("Status:"));
    filterPanel.add(statusTF);
    add(filterPanel, BorderLayout.NORTH);

    // Hide filters for now since it isn't implemented
    filterPanel.setVisible(false);

    sensorTable = new JTable(new SensorTableModel());
    JScrollPane scrollpane = new JScrollPane(sensorTable);
    add(scrollpane, BorderLayout.CENTER);

    JPanel refreshPanel = new JPanel();
    refreshPanel.setLayout(new GridLayout(1, 2));
    progressBar.setStringPainted(true);
    progressBar.setToolTipText("Time until refresh");
    refreshPanel.add(progressBar);

    final String[] refreshStrings = {"10 Seconds", "30 Seconds", "1 Minute", "2 Minutes", "5 Minutes"};
    final int[] refreshSeconds = {10, 30, 60, 120, 300};
    refreshCB = new JComboBox<String>(refreshStrings);
    refreshCB.setToolTipText("Refresh frequency");
    refreshCB.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        @SuppressWarnings("unchecked")
        JComboBox<String> cb = (JComboBox<String>) e.getSource();
        int index = cb.getSelectedIndex();

        int refreshInSeconds = refreshSeconds[index];

        Controller.getInstance().updateRefresh(refreshInSeconds);

      }
    });

    refreshPanel.add(refreshCB);

    add(refreshPanel, BorderLayout.SOUTH);

  }

  public JProgressBar getProgressBar() {
    return progressBar;
  }

}
