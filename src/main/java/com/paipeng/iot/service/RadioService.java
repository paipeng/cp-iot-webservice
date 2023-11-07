package com.paipeng.iot.service;

import com.paipeng.iot.entity.Radio;
import com.paipeng.iot.repository.RadioRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RadioService extends BaseService {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    @Autowired
    private RadioRepository radioRepository;

    public void getValidRadios() {
        logger.info("getValidRadios");
        List<Radio> radios = radioRepository.findRadiosByValid();

    }
}
