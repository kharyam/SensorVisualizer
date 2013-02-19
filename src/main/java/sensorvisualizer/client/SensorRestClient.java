package sensorvisualizer.client;

import java.util.ArrayList;
import java.util.List;


import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import sensorvisualizer.model.Sensor;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * This singleton class is used to interact with the iMonnit internet gateway
 * via its restful API.
 * 
 * @author kharym
 * 
 */
public class SensorRestClient {

	/**
	 * The API type (JSON or XML), which is appended to the base URI of the rest
	 * api
	 */
	private static final String API_TYPE_PATH = "/xml";

	/**
	 * The singleton instance of the client API
	 */
	private static SensorRestClient theInstance;

	private Client client;
	private WebResource webResource;
	private String username;
	private String password;

	private static Logger log = Logger.getLogger(SensorRestClient.class);

	/**
	 * The user authentication token
	 */
	private String token;

	/**
	 * Private constructor to support the singleton pattern
	 */
	private SensorRestClient() {

	}

	/**
	 * Creates the singleton instance of this class. Create the instance if it
	 * does not already exist.
	 * 
	 * @return the singleton instance
	 */
	public static SensorRestClient getInstance() {
		if (theInstance == null) {
			theInstance = new SensorRestClient();
		}
		return theInstance;
	}

	/**
	 * Initializes connection to the iMonnit internet gateway
	 * 
	 * @param baseUri
	 *            The URI location of the gateway, e.g. https://www.imonnit.com
	 * @param username
	 * @param password
	 * 
	 * @return true if succesful, false otherwise
	 */
	public boolean login(String baseUri, String username, String password) {

		this.username = username;
		this.password = password;

		client = Client.create();
		webResource = client.resource(baseUri + API_TYPE_PATH);

		return login();

	}

	/**
	 * Performs the login process using the cached username and password
	 * 
	 * @return true of successful, false otherwise
	 */
	private boolean login() {

		boolean result = false;

		ClientResponse response = webResource.path("GetAuthToken")
				.queryParam("username", this.username)
				.queryParam("password", this.password)
				.get(ClientResponse.class);

		if (response.getStatus() != 200) {
			log.error("Invalid response received on attempt to login.  Expected 200, got "
					+ response.getStatus());

		} else {

			token = getXmlValueUsingXpath(response, "//SensorRestAPI/Result");
			log.debug("Logged in user " + username + ", Token is " + token);
			response.close();
			
			result = wasLoginSuccessful();

		}

		return result;

	}

	
	/**
	 * 
	 * This method will make sure the token is still valid. If it isn't, it will
	 * log in to the API again if retry is true
	 * 
	 */
	private boolean validateLogin(boolean retry) {

		ClientResponse response = webResource.path("Logon").path(token)
				.get(ClientResponse.class);

		boolean result = false;
		
		if (response.getStatus() != 200) {
			log.error("Invalid response received on attempt to validate login.  Expected 200, got "
					+ response.getStatus());
		} else {
			String status = getXmlValueUsingXpath(response,
					"//SensorRestAPI/Result");
			if (!status.equals("Success")) {
				if (retry) {
					log.error("Token has expired.  Logging in again");
					login();
				} else {
					log.error("Token is invalid.");
				}
			} else {
				result=true;
			}
		}
		
		return result;
	}

	private void validateLogin() {
		validateLogin(true);
	}

	public boolean wasLoginSuccessful() {
		return validateLogin(false);
	}

	
	/**
	 * Returns the list of sensors, filtered by the input parameters below. All
	 * filters are optional (can be set to null)
	 * 
	 * 
	 * @param name
	 * @param networkID
	 * @param applicationID
	 * @param status
	 * @return all sensors matching the specified criteria
	 */
	public List<Sensor> getSensorList(String name, Integer networkID,
			Integer applicationID, Integer status) {

		ArrayList<Sensor> sensors = new ArrayList<Sensor>();

		validateLogin(); // Make sure the token is still valid

		WebResource resource = webResource.path("SensorList").path(token);

		if (name != null) {
			resource = resource.queryParam("name", name);
		}

		if (networkID != null) {
			resource = resource.queryParam("networkid", networkID.toString());
		}

		if (applicationID != null) {
			resource = resource.queryParam("applicationid",
					applicationID.toString());
		}

		if (status != null) {
			resource = resource.queryParam("applicationid", status.toString());
		}

		ClientResponse response = resource.get(ClientResponse.class);

		if (response.getStatus() != 200) {
			log.error("Invalid response received on attempt to retrieve sensor list.  Expected 200, got "
					+ response.getStatus());
		} else {
			populateSensors(response, sensors);
		}

		return sensors;
	}

	/**
	 * 
	 * Populate the list of sensors based on the web service response
	 * 
	 * @param response
	 * @param sensors
	 */
	private void populateSensors(ClientResponse response,
			ArrayList<Sensor> sensors) {

		String xmlString = (String) response.getEntity(String.class);
		try {
			Document document = DocumentHelper.parseText(xmlString);

			@SuppressWarnings("unchecked")
			List<Node> nodeList = (List<Node>) document
					.selectNodes("//SensorRestAPI/Result/APISensorList/APISensor");

			if (nodeList != null && !nodeList.isEmpty()) {
				for (Node node : nodeList) {
					Element element = (Element) node;
					Sensor sensor = new Sensor(
							element.attributeValue("SensorName"),
							Integer.parseInt(element.attributeValue("SensorID")),
							Integer.parseInt(element.attributeValue("MonnitApplicationID")),
							Integer.parseInt(element.attributeValue("CSNetID")),
							element.attributeValue("LastCommunicationDate"),
							element.attributeValue("NextCommunicationDate"),
							Integer.parseInt(element.attributeValue("LastDataMessageID")),
							Integer.parseInt(element.attributeValue("PowerSourceID")),
							Integer.parseInt(element.attributeValue("Status")),
							element.attributeValue("CanUpdate"),
							element.attributeValue("CurrentReading"),
							Integer.parseInt(element.attributeValue("BatteryLevel")),
							Integer.parseInt(element.attributeValue("SignalStrength")),
							element.attributeValue("AlertsActive"));
					sensors.add(sensor);
				}
			} else {
				log.error("Unable to retrieve sensor list, the xml returned was"
						+ xmlString);
			}

		} catch (Throwable t) {
			log.error("Failed to parse returned xml", t);

		}

	}

	/**
	 * Convenience method to get all sensors
	 * 
	 * @return all sensors
	 */
	public List<Sensor> getAllSensors() {
		return getSensorList(null, null, null, null);
	}

	/**
	 * Convenience method to retrieve a string value from XML using xpath
	 * 
	 * @param response
	 * @param xpathExpression
	 * @return
	 */
	private String getXmlValueUsingXpath(ClientResponse response,
			String xpathExpression) {

		String result = null;

		String xmlString = (String) response.getEntity(String.class);
		try {
			Document document = DocumentHelper.parseText(xmlString);
			Node node = document.selectSingleNode(xpathExpression);

			if (node != null) {
				result = node.getStringValue();
			} else {
				log.error("Unable to retrieve string using xpath "
						+ xpathExpression + ". The XML returned was: "
						+ xmlString);
			}

		} catch (Throwable t) {
			log.error("Failed to parse returned xml", t);

		}

		return result;

	}

}
