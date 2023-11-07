package com.paipeng.iot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paipeng.iot.entity.ContactScanCode;
import com.paipeng.iot.entity.Device;
import com.paipeng.iot.entity.Radio;
import com.paipeng.iot.entity.User;
import com.paipeng.iot.mqtt.gateway.MqttGateway;
import com.paipeng.iot.mqtt.model.CPIOTBase;
import com.paipeng.iot.mqtt.model.CPIOTPagerMessage;
import com.paipeng.iot.mqtt.model.CPIOTPing;
import com.paipeng.iot.mqtt.model.CPIOTRadio;
import com.paipeng.iot.repository.ContactScanCodeRepository;
import com.paipeng.iot.repository.DeviceRepository;
import com.paipeng.iot.repository.RadioRepository;
import com.paipeng.iot.repository.UserRepository;
import com.paipeng.iot.util.Font2ImageUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

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

    @Autowired
    private RadioRepository radioRepository;

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
        if (contactScanCode != null && checkContactScanCodeValidation(contactScanCode)) {
            // TODO check scan code validation

            cpiotPagerMessage.setSender(contactScanCode.getSendUser().getUsername());
            cpiotPagerMessage.setReceiver(contactScanCode.getReceiveUser().getUsername());

            // convert text to 1bit pixel
            //byte[] data = Font2ImageUtil.text2Pixel(cpiotPagerMessage.getMessage(), 26);

            //cpiotPagerMessage.setTextPixelBase64(Base64.getEncoder().encodeToString(data));
            cpiotPagerMessage.setTextCount(cpiotPagerMessage.getMessage().length());

            if (contactScanCode.getReceiveUser().getDevices() != null && contactScanCode.getReceiveUser().getDevices().size() > 0) {
                for (Device device : contactScanCode.getReceiveUser().getDevices()) {
                    if (device.isPager()) {
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            String topic = "CP_IOT/" + device.getUdid() + "/PAGER_MESSAGE";
                            String jsonData = objectMapper.writeValueAsString(cpiotPagerMessage);
                            logger.info("send pager message ping");
                            logger.info("sendToMqtt topic: " + topic);
                            logger.info("sendToMqtt data: " + jsonData);
                            mqttGateway.sendToMqtt(jsonData, topic);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    public CPIOTPagerMessage pagerValidateContactScanCode(String uuid) {
        ContactScanCode contactScanCode = contactScanCodeRepository.findByUuid(uuid).orElse(null);
        if (contactScanCode != null && checkContactScanCodeValidation(contactScanCode)) {
            CPIOTPagerMessage cpiotPagerMessage = new CPIOTPagerMessage();
            cpiotPagerMessage.setUuid(uuid);
            cpiotPagerMessage.setSender(contactScanCode.getSendUser().getUsername());
            cpiotPagerMessage.setReceiver(contactScanCode.getReceiveUser().getUsername());

            return cpiotPagerMessage;
        } else {
            throw new RuntimeException();
        }
    }

    private boolean checkContactScanCodeValidation(ContactScanCode contactScanCode) {
        return (contactScanCode.isValid());
    }

    public void getRadios(CPIOTBase device) {
        logger.info("getValidRadios: " + device.getUdid());
        List<Radio> radios = radioRepository.findRadiosByValid();

        CPIOTRadio cpiotRadio = new CPIOTRadio();
        cpiotRadio.setRadios(radios);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String topic = "CP_IOT/" + device.getUdid() + "/RADIO";
            String jsonData = objectMapper.writeValueAsString(cpiotRadio);
            logger.info("send radio list to device");
            logger.info("sendToMqtt topic: " + topic);
            logger.info("sendToMqtt data: " + jsonData);
            mqttGateway.sendToMqtt(jsonData, topic);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void radioPlay(String udid) {
        Device device = deviceRepository.findByUdid(udid).orElse(null);
        if (device != null) {
            CPIOTRadio cpiotRadio = new CPIOTRadio();
            List<Radio> radios = radioRepository.findRadiosByValid();
            if (radios != null && !radios.isEmpty()) {
                cpiotRadio.setRadios(radios);
            }
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String topic = "CP_IOT/" + device.getUdid() + "/RADIO/PLAY";
                String jsonData = objectMapper.writeValueAsString(cpiotRadio);
                logger.info("send radio list to device");
                logger.info("sendToMqtt topic: " + topic);
                logger.info("sendToMqtt data: " + jsonData);
                mqttGateway.sendToMqtt(jsonData, topic);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void radioStop(String udid) {
        Device device = deviceRepository.findByUdid(udid).orElse(null);
        if (device != null) {
            CPIOTRadio cpiotRadio = new CPIOTRadio();

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String topic = "CP_IOT/" + device.getUdid() + "/RADIO/STOP";
                String jsonData = objectMapper.writeValueAsString(cpiotRadio);
                logger.info("send radio list to device");
                logger.info("sendToMqtt topic: " + topic);
                logger.info("sendToMqtt data: " + jsonData);
                mqttGateway.sendToMqtt(jsonData, topic);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
