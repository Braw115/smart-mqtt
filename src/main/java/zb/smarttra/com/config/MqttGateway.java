
package zb.smarttra.com.config;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {

    boolean sendToMqtt(String payload);

    boolean sendToMqtt(String payload,@Header(MqttHeaders.TOPIC) String topic);

    boolean sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);

    boolean sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos,  @Header(MqttHeaders.RETAINED) String retain,String payload);
}

