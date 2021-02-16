package com.ntico.mqtt;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class CustomMqttClient {

	//   ============================ Constants ==============================

	//   ============================ Constants ==============================

//	private String BROKER_URL = "wss:/161.97.84.200.1883";
	
	private String BROKER = "tcp://161.97.84.200:1883";
	private String CLIENT_ID = "Nti-CO² " + UUID.randomUUID();

	//	 =========================== Attributes ==============================

	private MqttClient m_client;
	private MqttCallback m_appCallback;
	private String m_topic;

	//	 =========================== Constructor =============================

	public CustomMqttClient() throws MqttException {
		this("#");
	}
	
	public CustomMqttClient(String topic) throws MqttException {
		m_client = new MqttClient(BROKER, CLIENT_ID, new MemoryPersistence());
		m_topic = topic;
	}
	
	//	 ========================== Access methods ===========================

	//	 ========================= Treatment methods =========================
	
	public void connect() throws MqttSecurityException, MqttException {
		
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		
		System.out.println("Connecting to broker: " + BROKER);
		
//		connOpts.setUserName("myUserName");
//		connOpts.setPassword("myPassword");

		connOpts.setAutomaticReconnect(true);
		connOpts.setMaxReconnectDelay(120);
		connOpts.setKeepAliveInterval(60);
		
		m_client.setCallback(new MqttCallbackExtended() {

			public void connectionLost(Throwable cause) {}
			public void deliveryComplete(IMqttDeliveryToken token) {}

			public void messageArrived(String topic, MqttMessage message) throws Exception {
				if (m_appCallback != null) {
					m_appCallback.messageArrived(topic, message);
				}
			}

			public void connectComplete(boolean reconnect, String serverURI) {
				System.out.println("Connected");
				try { register(); }
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			
		});
		
		m_client.connect(connOpts);
		register();
	}

	public void setCallback(MqttCallback callback) {
		m_appCallback = callback;
	}
	
	private void register() throws MqttException {
		m_client.subscribe(m_topic);
	}

	public void publish(String topic, byte[] payload, int qos, boolean retained) throws MqttPersistenceException, MqttException {
		m_client.publish(topic, payload, qos, retained);
	}
}

