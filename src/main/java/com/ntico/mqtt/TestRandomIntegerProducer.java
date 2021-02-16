package com.ntico.mqtt;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class TestRandomIntegerProducer {

	//   ============================ Constants ==============================

	//	 =========================== Attributes ==============================

	private CustomMqttClient m_client;

	//	 =========================== Constructor =============================

	public TestRandomIntegerProducer(CustomMqttClient client) {
		m_client = client;
	}

	//	 ========================== Access methods ===========================

	//	 ========================= Treatment methods =========================
	
	public static void main(String[] args) throws MqttException {
			
		CustomMqttClient client = new CustomMqttClient("myTopic");
		client.connect();
		
		TestRandomIntegerProducer test = new TestRandomIntegerProducer(client);
		
		test.schedule(5 * 1000l, () -> { 
			try { test.sendRandomInt(); }
			catch (Exception ex) {
				System.err.println("Error while sending message : " + ex.getMessage());
				ex.printStackTrace();
			}
		} );
	}
	
	protected void sendRandomInt() throws MqttException {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		final int randomInt = new Random().nextInt();
		buffer.putInt(randomInt);

		System.out.println(">> Sending message with integer : " + randomInt);

		m_client.publish("myTopic", buffer.array(), 0, false);
	}
	
	void schedule(long interval, Runnable delay) {
		new Timer().schedule(new TimerTask() {
			public void run() { delay.run(); }
		}, 0l, interval);
	}

}

