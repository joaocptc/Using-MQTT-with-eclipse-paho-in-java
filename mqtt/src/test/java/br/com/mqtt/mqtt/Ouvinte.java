package br.com.mqtt.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * 
 * @author João
 *
 */

public class Ouvinte implements IMqttMessageListener {
	
	public Ouvinte(ClienteMQTT clienteMQTT, String topicos, int QoS) {
		clienteMQTT.subscribe(QoS, this, topicos);
	}

	public void messageArrived(String topico, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Mensagem recebida:");
		System.out.println("\tTópico: " + topico);
		System.out.println("\tMensagem: " + new String(message.getPayload()));
		System.out.println("");
	}
}
