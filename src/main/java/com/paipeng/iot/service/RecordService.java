package com.paipeng.iot.service;

import com.paipeng.iot.entity.Device;
import com.paipeng.iot.entity.Record;
import com.paipeng.iot.entity.RecordType;
import com.paipeng.iot.mqtt.model.CPIOTPing;
import com.paipeng.iot.repository.DeviceRepository;
import com.paipeng.iot.repository.RecordRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RecordService extends BaseService {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    public List<Record> query() {
        logger.info("query");
        List<Record> records =  recordRepository.findAll();
        logger.info("records: " + records);
        return records;
    }
    public Record query(Long id) {
        return recordRepository.findById(id).orElseThrow();
    }

    public List<Record> queryByDevice(Long id) {
        return recordRepository.findAllByDeviceId(id);
    }

    public void updatePong(CPIOTPing cpiotPing) {
        logger.info("updatePong: " + cpiotPing);
        Device device = deviceRepository.findByUuid(cpiotPing.getDeviceUuid()).orElse(null);
        if (device != null) {
            Record record = new Record();
            record.setDevice(device);
            record.setState(cpiotPing.getState());
            record.setRecordType(RecordType.PING);
            recordRepository.saveAndFlush(record);
        } else {
            logger.error("device not found!");
        }
    }
}