package com.ntico.mqtt;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestStockOHLCProducer {

	//   ============================ Constants ==============================

	//	 =========================== Attributes ==============================

	private CustomMqttClient m_client;

	//	 =========================== Constructor =============================

	public TestStockOHLCProducer(CustomMqttClient client) {
		m_client = client;
	}

	//	 ========================== Access methods ===========================

	//	 ========================= Treatment methods =========================
	
	public static void main(String[] args) throws MqttException {
			
		CustomMqttClient client = new CustomMqttClient();
		client.connect();
		
		TestStockOHLCProducer test = new TestStockOHLCProducer(client);
		
		test.schedule(5 * 60 * 1000l, () -> { 
			try { test.sendOHLC(); }
			catch (Exception ex) {
				System.err.println("Error while sending message : " + ex.getMessage());
				ex.printStackTrace();
			}
		} );
	}
	
	protected void sendOHLC() throws Exception {
		List<OhlcDTO> listOHLC = AlphaConnect.getOHLC("IBM");
		ObjectMapper mapper = new ObjectMapper();
		for (OhlcDTO ohlcDTO : listOHLC) {
			final String jsonSerialized = mapper.writeValueAsString(ohlcDTO);
			m_client.publish("stockTopic/IBM", jsonSerialized.getBytes(Charset.forName("UTF-8")), 0, false);
		}
	}
	
	void schedule(long interval, Runnable delay) {
		new Timer().schedule(new TimerTask() {
			public void run() { delay.run(); }
		}, 0l, interval);
	}
}

