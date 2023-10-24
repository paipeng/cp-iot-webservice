package com.paipeng.iot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paipeng.iot.entity.User;
import com.paipeng.iot.mqtt.gateway.MqttGateway;
import com.paipeng.iot.mqtt.model.CPIOTPing;
import org.antlr.v4.runtime.misc.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/mqtts")
public class MqttController {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    @Autowired
    private MqttGateway mqttGateway;

    @PostMapping("/send")
    public String sendMsg(String data) {
        mqttGateway.sendToMqtt(data);
        return "success";
    }

    /**
     * 发送mqtt消息
     *
     * @param data  负载
     * @param topic 话题
     * @return
     */
    @PostMapping("/topic/send")
    public String sendMsg(String data, String topic) {
        mqttGateway.sendToMqtt(data, topic);
        return "success";
    }


    @GetMapping(value = "/ping/{udid}", produces = {"application/json;charset=UTF-8"})
    public void testMqttPing(@NotNull @PathVariable("udid") String udid) {
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

    @GetMapping(value = "/ping", produces = {"application/json;charset=UTF-8"})
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
}
