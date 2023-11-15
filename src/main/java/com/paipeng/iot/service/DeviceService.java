package com.paipeng.iot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paipeng.iot.entity.Device;
import com.paipeng.iot.entity.Record;
import com.paipeng.iot.entity.RecordType;
import com.paipeng.iot.entity.User;
import com.paipeng.iot.mqtt.gateway.MqttGateway;
import com.paipeng.iot.mqtt.model.CPIOMessageBoard;
import com.paipeng.iot.mqtt.model.CPIOTLed;
import com.paipeng.iot.repository.DeviceRepository;
import com.paipeng.iot.repository.RecordRepository;
import com.paipeng.iot.repository.UserRepository;
import com.paipeng.iot.util.Font2ImageUtil;
import com.paipeng.iot.util.ImageUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class DeviceService extends BaseService {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private MqttGateway mqttGateway;

    public List<Device> get() {
        return deviceRepository.findAll();
    }

    public Device getById(Long id) {
        return deviceRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }

    public Device save(Device device) {
        logger.info("save: " + device);
        // set user
        User user = getUserFromSecurity();

        device.setUsers(List.of(userRepository.findById(user.getId()).orElse(null)));
        device = deviceRepository.saveAndFlush(device);
        return device;
    }

    public Device update(Device device) {
        device = deviceRepository.saveAndFlush(device);
        return device;
    }

    public void delete(Long id) {
        Device device = deviceRepository.findById(id).orElseThrow(() -> new RuntimeException());
        deviceRepository.delete(device);
    }

    public Device updateLedState(Long id, int state) {
        logger.info("updateLedState: " + id + " state: " + state);
        Device device = deviceRepository.findById(id).orElseThrow(() -> new RuntimeException());
        Record record = new Record();
        record.setDevice(device);
        record.setState(state);
        record.setRecordType(RecordType.LED);
        recordRepository.saveAndFlush(record);

        // MQTT send command to IoT
        String topic = "CP_IOT/" + device.getUdid() + "/LED";
        logger.info("send mqtt to topic: " + topic);


        CPIOTLed cpiotLed = new CPIOTLed();
        cpiotLed.setUdid(device.getUdid());
        cpiotLed.setState(state);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(cpiotLed);
            logger.info("sendToMqtt json: " + json);
            mqttGateway.sendToMqtt(json, topic);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return device;
    }

    public Device updateMessageBoard(Long id, CPIOMessageBoard messageBoard) throws IOException, FontFormatException {
        logger.info("updateMessageBoard: " + id + " messageBoard: " + messageBoard);
        Device device = deviceRepository.findById(id).orElseThrow(() -> new RuntimeException());
        Record record = new Record();
        record.setDevice(device);
        record.setMessage(messageBoard.getMessage());
        record.setRecordType(RecordType.MESSAGE_BOARD);


        // convert text to 1bit pixel
        byte[][] data = Font2ImageUtil.text2Parola(messageBoard.getMessage(), 14);

        assert data != null;
        ImageUtil.printParolaMatrix(data, 12);
        messageBoard.setTextPixelBase64(Base64.getEncoder().encodeToString(data[0]));
        messageBoard.setTextPixel2Base64(Base64.getEncoder().encodeToString(data[1]));


        recordRepository.saveAndFlush(record);

        // MQTT send command to IoT
        String topic = "CP_IOT/" + device.getUdid() + "/MESSAGE_BOARD";
        logger.info("send mqtt to topic: " + topic);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(messageBoard);
            logger.info("sendToMqtt json: " + json);
            mqttGateway.sendToMqtt(json, topic);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return device;
    }
}
