package com.paipeng.iot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paipeng.iot.entity.ContactScanCode;
import com.paipeng.iot.entity.Device;
import com.paipeng.iot.entity.User;
import com.paipeng.iot.mqtt.gateway.MqttGateway;
import com.paipeng.iot.mqtt.model.CPIOTPagerMessage;
import com.paipeng.iot.mqtt.model.CPIOTPing;
import com.paipeng.iot.repository.ContactScanCodeRepository;
import com.paipeng.iot.repository.DeviceRepository;
import com.paipeng.iot.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqttService extends BaseService {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    private MqttGateway mqttGateway;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ContactScanCodeRepository contactScanCodeRepository;

    @Autowired
    private UserRepository userRepository;

    public void sendMsg(String data) {
        mqttGateway.sendToMqtt(data);
    }
    public void sendMsg(String data, String topic) {
        mqttGateway.sendToMqtt(data, topic);
    }

    public void testMqttPing(String udid) {
        CPIOTPing cpiotPing = new CPIOTPing();

        cpiotPing.setUdid(udid);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String topic = "CP_IOT/" + udid + "/PING";
            String data = objectMapper.writeValueAsString(cpiotPing);
            logger.info("sendToMqtt topic: " + topic);
            logger.info("sendToMqtt data: " + data);
            mqttGateway.sendToMqtt(data, topic);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void testMqttPings() {
        CPIOTPing cpiotPing = new CPIOTPing();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String topic = "CP_IOT/PING";
            String data = objectMapper.writeValueAsString(cpiotPing);
            logger.info("send broadcasting ping");
            logger.info("sendToMqtt topic: " + topic);
            logger.info("sendToMqtt data: " + data);
            mqttGateway.sendToMqtt(data, topic);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPagerMessage(CPIOTPagerMessage cpiotPagerMessage) {
        logger.info("sendPagerMessage");
        // 验证 有效性
        ContactScanCode contactScanCode = contactScanCodeRepository.findByUuid(cpiotPagerMessage.getUuid()).orElse(null);
        if (contactScanCode != null) {
            // TODO check scan code validation

            cpiotPagerMessage.setSender(contactScanCode.getSendUser().getUsername());
            cpiotPagerMessage.setReceiver(contactScanCode.getReceiveUser().getUsername());

            if (contactScanCode.getReceiveUser().getDevices() != null && contactScanCode.getReceiveUser().getDevices().size() > 0) {
                for (Device device : contactScanCode.getReceiveUser().getDevices()) {
                    if (device.isPager()) {
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            String topic = "CP_IOT/" + device.getUdid() + "/PAGER_MESSAGE";
                            String data = objectMapper.writeValueAsString(cpiotPagerMessage);
                            logger.info("send pager message ping");
                            logger.info("sendToMqtt topic: " + topic);
                            logger.info("sendToMqtt data: " + data);
                            mqttGateway.sendToMqtt(data, topic);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

        }



    }
}
