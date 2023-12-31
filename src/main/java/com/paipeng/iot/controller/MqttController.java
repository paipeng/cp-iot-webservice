package com.paipeng.iot.controller;

import com.paipeng.iot.mqtt.model.CPIOTPagerMessage;
import com.paipeng.iot.service.MqttService;
import jakarta.servlet.http.HttpServletResponse;
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
    private MqttService mqttService;


    @PostMapping("/send")
    public String sendMsg(String data) {
        mqttService.sendMsg(data);
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
        mqttService.sendMsg(data, topic);
        return "success";
    }


    @GetMapping(value = "/ping/{udid}", produces = {"application/json;charset=UTF-8"})
    public void testMqttPing(@NotNull @PathVariable("udid") String udid) {
        mqttService.testMqttPing(udid);
    }

    @GetMapping(value = "/ping", produces = {"application/json;charset=UTF-8"})
    public void testMqttPings() {
        mqttService.testMqttPings();

    }

    @GetMapping(value = "/pager/{uuid}/validate", produces = {"application/json;charset=UTF-8"})
    public CPIOTPagerMessage pagerValidateContactScanCode(@NotNull @PathVariable("uuid") String uuid) {
        return mqttService.pagerValidateContactScanCode(uuid);

    }

    @PostMapping(value = "/pager", produces = {"application/json;charset=UTF-8"})
    public void sendPagerMessage(@NotNull @RequestBody CPIOTPagerMessage cpiotPagerMessage, HttpServletResponse httpServletResponse) {
        mqttService.sendPagerMessage(cpiotPagerMessage);

    }

    @GetMapping(value = "/radio/{udid}/play/{id}", produces = {"application/json;charset=UTF-8"})
    public void radioPlay(@NotNull @PathVariable("udid") String udid, @NotNull @PathVariable("id") Long id) {
        mqttService.radioPlay(udid, id);
    }

    @GetMapping(value = "/radio/{udid}/stop", produces = {"application/json;charset=UTF-8"})
    public void radioStop(@NotNull @PathVariable("udid") String udid) {
        mqttService.radioStop(udid);
    }

}
