package com.paipeng.iot.service;

import com.paipeng.iot.entity.Record;
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
}