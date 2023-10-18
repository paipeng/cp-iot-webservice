package com.paipeng.iot.mqtt;

import com.paipeng.iot.mqtt.handle.ReceiveMessageHandler;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@IntegrationComponentScan
public class MqttConfig {
    @Autowired
    private MqttProperties mqttProp;

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(mqttProp.getUsername());
        options.setPassword(mqttProp.getPassword().toCharArray());
        options.setServerURIs(mqttProp.getHostUrl());
        return options;
    }

    /**
     * 创建MqttPahoClientFactory，设置MQTT Broker连接属性，如果使用SSL验证，也在这里设置。
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions());
        return factory;
    }

    /** 发送通道 */
    @Bean
    public MessageChannel mqttOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutputChannel")
    public MessageHandler mqttOutputHandler() {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(mqttProp.getClientId(), mqttClientFactory());
        // 如果设置成true，即异步，发送消息时将不会阻塞。
        handler.setAsync(true);
        handler.setDefaultTopic(mqttProp.getDefaultTopic());
        return handler;
    }


    /** 接收通道 */
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    /** 配置client监听的topic */
    @Bean
    public MessageProducer messageProducer() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(mqttProp.getClientId() + "_input", mqttClientFactory(), mqttProp.getSubscriptionTopic());
        adapter.setCompletionTimeout(mqttProp.getConnectionTimeout());
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler mqttInputHandler() {
        return new ReceiveMessageHandler();
    }
}
