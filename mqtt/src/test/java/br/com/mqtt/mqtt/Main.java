package br.com.mqtt.mqtt;

import java.text.SimpleDateFormat;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Main {
	
	public static void main (String[] args) throws InterruptedException, MqttException {
		ClienteMQTT clienteMQTT = new ClienteMQTT("tcp://localhost:1883", null, null);
		clienteMQTT.iniciar();
		/*
		new Ouvinte(clienteMQTT, "#", 0);
		while ( true ) { // publisher
			Thread.sleep(1000);
			String mensagem = "Mensagem enviada em " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(System.currentTimeMillis());
			clienteMQTT.publicar("cliente", mensagem.getBytes(), 0);
		}*/
		// caso queira publicar no console 
		// mosquitto_pub -h localhost -p 1883 -t "temperatura" -m "45"
		MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
		client.setCallback( clienteMQTT );
		client.connect();
		client.subscribe("temperatura");
	}
}
