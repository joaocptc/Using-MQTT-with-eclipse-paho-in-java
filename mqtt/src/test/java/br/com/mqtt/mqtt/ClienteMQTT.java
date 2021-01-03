package br.com.mqtt.mqtt;

import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

/**
 * 
 * @author João
 *
 */

public class ClienteMQTT implements MqttCallbackExtended {
	
	private final String serverURI;
	private MqttClient client;
	private final MqttConnectOptions mqttOptions;
	
	public ClienteMQTT(String serverURI, String usuario, String senha) {
		this.serverURI = serverURI;
		// set properties
		mqttOptions = new MqttConnectOptions();
		mqttOptions.setMaxInflight(200);
		mqttOptions.setConnectionTimeout(3);
		mqttOptions.setKeepAliveInterval(10);
		mqttOptions.setAutomaticReconnect(true);
		mqttOptions.setCleanSession(false);
		if ( usuario != null && senha != null) {
			mqttOptions.setUserName(usuario);
			mqttOptions.setPassword(senha.toCharArray());
		}	
	}
	
	public IMqttToken subscribe(int QoS, IMqttMessageListener gestorMensagemMQTT, String... topicos) {
		
		if ( client == null || topicos.length == 0 )
			return null;
		
		int tamanho = topicos.length;
		int[] qoss = new int[tamanho];
		IMqttMessageListener[] listeners = new IMqttMessageListener[tamanho];
		
		for (int i = 0; i < tamanho; i++) {
			qoss[i] = QoS;
			listeners[i] = gestorMensagemMQTT;
		}
		try {
			return client.subscribeWithResponse(topicos, qoss, listeners);
		} catch (MqttException e) {
			System.out.println(String.format("Falha ao inscrever nos tópicos %s - %s", Arrays.asList(topicos), e));
			return null;
		}
	}
	
	public void unsubscribe(String[] topicos) {
		if ( client == null || topicos.length == 0 )
			return;
		
		try {
			client.unsubscribe(topicos);
		} catch ( MqttException e ) {
			System.out.println(String.format("Falha ao cancelar inscrição nos tópicos %s - %s", Arrays.asList(topicos), e));
		}
	}
	
	public void iniciar() {
		try {
			System.out.println("Conectando ao bloker MQTT em " + serverURI);
			client = new MqttClient(serverURI, String.format("cleinte_java_$d", System.currentTimeMillis()), new MqttDefaultFilePersistence(System.getProperty("java.io.tmpdir")));
			client.setCallback(this);
			client.connect(mqttOptions);
		} catch ( MqttException e ) {
			System.out.println("Falha ao conectar ao bloker MQTT em " + serverURI + " - " + e);
		}
	}
	
	public void finalizar() {
		if ( client == null || !client.isConnected() )
			return;
		try {
			client.disconnect();
			client.close();
		} catch ( MqttException e ) {
			System.out.println("Falha ao desconectar do bloquer MQTT - " + e);
		}
	}
	
	public void publicar(String topico, byte[] payload, int QoS) {
		publicar(topico, payload, QoS, false);
	}
	
	public synchronized void publicar(String topico, byte[] payload, int QoS, boolean retained) {
		try {
			if ( client.isConnected() ) {
				client.publish(topico, payload, QoS, retained);
				System.out.println(String.format("Tópico %s publicado. %dB", topico, payload.length));
			}
			else
				System.out.println("Cliente desconectado, não foi possível publicar o tópico" + topico);
		} catch ( MqttException e ) {
			System.out.println("Erro ao publicar " + topico + " - " + e);
		}
	}

	public void connectionLost(Throwable cause) {
		System.out.print("Conexão com o broker perdida - " + cause);
	}

	public void connectComplete(boolean reconnect, String serverURI) {
		System.out.println("Cliente MQTT " + (reconnect? "reconectado" : "contectado") + " com o broker " + serverURI);	
	}
	
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("\tMensagem recebida: \n\t" + new String( message.getPayload() ));
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
	}
}
