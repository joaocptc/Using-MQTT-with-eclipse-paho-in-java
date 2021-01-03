# Using-MQTT-with-eclipse-paho-in-java
A simple exemple of use MQTT in java


Para uso:
Baixar o mosquitto e installar;
Abrir o terminal e navegar até a pasta onde o mosquitto foi instalado;
Usar o comando < mosquitto -v > para subir o servidor
importar o projeto no eclipse ou em outra IDE de preferência
Executar a aplicação
Abra outro terminal, navegue até o diretório do mosquitto e execute o comando < mosquitto_pub -h localhost -p 1883 -t "temperatura" -m "45" > para publicar