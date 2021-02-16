package com.ntico.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestStockOHLCConsumer implements MqttCallback {

	//   ============================ Constants ==============================

	//	 =========================== Attributes ==============================

	//	 =========================== Constructor =============================

	public TestStockOHLCConsumer() {
	}

	//	 ========================== Access methods ===========================

	//	 ========================= Treatment methods =========================
	
	public static void main(String[] args) throws MqttException {
			
		CustomMqttClient client = new CustomMqttClient("stockTopic/#");
		client.connect();
		
		TestStockOHLCConsumer test = new TestStockOHLCConsumer();
		client.setCallback(test);
	}
	
	// =================================== MqttCallback impl ======================================

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		try {
			if (topic.startsWith("stockTopic")) {
				ObjectMapper mapper = new ObjectMapper();
				OhlcDTO dto = mapper.readValue(message.getPayload(), OhlcDTO.class);
				String symbol = topic.split("/")[1];
				System.out.println("<< OHLC received for stock " + symbol + " : " + dto);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}
}

