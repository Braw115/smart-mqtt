
package zb.smarttra.com.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import zb.smarttra.com.utils.MqttConst;

@Configuration
public class MqttInboundConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttInboundConfiguration.class);

    @Autowired
    private MqttConfiguration mqttProperties;

    @Bean
    public MessageChannel mqttInputChannel() {

        return new DirectChannel();
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        String[] array = mqttProperties.getUrl().split(",");
        factory.setUserName(mqttProperties.getUsername());
        factory.setPassword(mqttProperties.getPassword());
        factory.setKeepAliveInterval(mqttProperties.getKeepAliveInterval());
        factory.setServerURIs(array);
        factory.setCleanSession(false);
        return factory;
    }

    @Bean
    public MessageProducer inbound() {
        String[] inboundTopics = mqttProperties.getTopics().split(",");
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                MqttConst.MQTT_CLIENTID,mqttClientFactory(), inboundTopics);
        adapter.setCompletionTimeout(5000);
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {

        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                LOGGER.info("回调指令："+message);
            }
        };
    }
}

