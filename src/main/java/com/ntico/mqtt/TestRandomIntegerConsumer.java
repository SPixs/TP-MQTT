package com.ntico.mqtt;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class TestRandomIntegerConsumer implements MqttCallbackExtended {

	//   ============================ Constants ==============================

	//	 =========================== Attributes ==============================

	//	 =========================== Constructor =============================

	public TestRandomIntegerConsumer() {
	}

	//	 ========================== Access methods ===========================

	//	 ========================= Treatment methods =========================
	
	public static void main(String[] args) throws MqttException {
			
		CustomMqttClient client = new CustomMqttClient("myTopic");
		client.connect();
		
		TestRandomIntegerConsumer test = new TestRandomIntegerConsumer();
		client.setCallback(test);
	}
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		if (!"myTopic".equals(topic)) {
			System.err.println("Unexpected message on topic : " + topic);
			return;
		}
		
		ByteBuffer buffer = ByteBuffer.wrap(message.getPayload());
		System.out.println("<< Integer message received : " + buffer.getInt());
	}

	public void connectionLost(Throwable cause) {}
	public void deliveryComplete(IMqttDeliveryToken token) {}
	public void connectComplete(boolean reconnect, String serverURI) {}
}

