package com.paipeng.iot.mqtt.handle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

public class ReceiveMessageHandler implements MessageHandler {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        logger.info("handleMessage");
        String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
        logger.info("topic: " + topic);
        if ("hello".equalsIgnoreCase(topic)) {
            System.out.println("hello, " + message.getPayload().toString());
        } else {
            System.out.println("hi, " + message.getPayload().toString());
        }
    }

}
