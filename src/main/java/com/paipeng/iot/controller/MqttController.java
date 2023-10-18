package com.paipeng.iot.controller;

import com.paipeng.iot.mqtt.gateway.MqttGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/mqtts")
public class MqttController {
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
}
