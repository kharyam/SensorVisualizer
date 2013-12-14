package sensorvisualizer.view;

import sensorvisualizer.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

  private static final long serialVersionUID = 1L;

  private JTextField gatewayUrlTF = new JTextField("https://www.imonnit.com");
  private JTextField usernameTF = new JTextField("Guest");
  private JPasswordField passwordTF = new JPasswordField();

  public LoginFrame() {
    super("Login");

    setLayout(new GridLayout(4, 2));
    setResizable(false);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JLabel gatewayLabel = new JLabel("Gateway URL:");
    JLabel userNameLabel = new JLabel("User Name:");
    JLabel passwordLabel = new JLabel("Password:");

    getContentPane().add(gatewayLabel);
    getContentPane().add(gatewayUrlTF);
    getContentPane().add(userNameLabel);
    getContentPane().add(usernameTF);
    getContentPane().add(passwordLabel);
    getContentPane().add(passwordTF);
    getContentPane().add(new JLabel());

    JButton loginBtn = new JButton("Login");
    loginBtn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        Controller.getInstance().login(gatewayUrlTF.getText(), usernameTF.getText(), passwordTF.getPassword());
      }
    });

    getContentPane().add(loginBtn);

    pack();

    // Get the size of the screen
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    // Determine the new location of the window
    int w = getSize().width;
    int h = getSize().height;
    int x = (dim.width - w) / 2;
    int y = (dim.height - h) / 4;

    // Move the window
    setLocation(x, y);

  }
}
