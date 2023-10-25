package com.paipeng.iot.service;

import com.paipeng.iot.entity.Device;
import com.paipeng.iot.entity.Record;
import com.paipeng.iot.entity.RecordType;
import com.paipeng.iot.entity.User;
import com.paipeng.iot.mqtt.gateway.MqttGateway;
import com.paipeng.iot.repository.DeviceRepository;
import com.paipeng.iot.repository.RecordRepository;
import com.paipeng.iot.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
        mqttGateway.sendToMqtt(""+ state, topic);
        return device;
    }
}
