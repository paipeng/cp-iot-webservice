package com.paipeng.iot.mqtt.handle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paipeng.iot.mqtt.model.CPIOTBase;
import com.paipeng.iot.mqtt.model.CPIOTPing;
import com.paipeng.iot.mqtt.model.CPIOTTemperature;
import com.paipeng.iot.service.MqttService;
import com.paipeng.iot.service.RecordService;
import com.paipeng.iot.service.RadioService;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
public class ReceiveMessageHandler implements MessageHandler {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    private RecordService recordService;

    @Autowired
    private MqttService mqttService;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        logger.info("handleMessage MQTT");
        String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
        logger.info("topic: " + topic);
        logger.info("message: " + message.getPayload().toString());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            if (topic.equals("CP_IOT/PONG")) {
                logger.info("CP_IOT/PONG");
                CPIOTPing cpiotPing = objectMapper.readValue(message.getPayload().toString(), CPIOTPing.class);
                if (cpiotPing != null) {
                    recordService.updatePong(cpiotPing);
                }
            } else if (topic.equals("CP_IOT/TEMPERATURE")) {
                logger.info("CP_IOT/TEMPERATURE");
                CPIOTTemperature cpiotTemperature = objectMapper.readValue(message.getPayload().toString(), CPIOTTemperature.class);
                if (cpiotTemperature != null) {
                    recordService.updateTemperature(cpiotTemperature);
                }
            } else if (topic.equals("CP_IOT/RADIO")) {
                logger.info("CP_IOT/RADIO");
                CPIOTBase cpiotBase = objectMapper.readValue(message.getPayload().toString(), CPIOTBase.class);
                if (cpiotBase != null) {
                    mqttService.getRadios(cpiotBase);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
