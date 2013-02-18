package sensorvisualizer.controller;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

/**
 * This class sends an email via gmail
 * @author kharym
 *
 */
public class GmailSender {

	private static Logger log = Logger.getLogger(GmailSender.class);
	
	/**
	 * This method sends an email via gmail
	 * 
	 * @param address
	 * @param subject
	 * @param body
	 */
	public static void sendGmail(String address, String subject, String body) {

		final String username = Controller.getInstance().getConfigValue(Controller.GMAIL_SENDER_ADDRESS_PROPERTY);
		final String password = Controller.getInstance().getConfigValue(Controller.GMAIL_SENDER_PASSWORD_PROPERTY);

		log.info("Sending email\nTo: "+address+"\nFrom: "+username+"\nSubject: "+subject+"\nBody: "+body+"\n");
		
		if (username == null || password==null) {
			log.error("No username/password set for sending email.  Please add a gmail username and password to the configuration file using "+ Controller.GMAIL_SENDER_ADDRESS_PROPERTY + " and " + Controller.GMAIL_SENDER_PASSWORD_PROPERTY);	
		} else {

			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");

			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			try {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("username"));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(address));
				message.setSubject(subject);
				message.setText(body);

				Transport.send(message);

			} catch (Throwable t) {
				log.error("Failed to send email to "+address,t);
			}
		}

	}
	
}
