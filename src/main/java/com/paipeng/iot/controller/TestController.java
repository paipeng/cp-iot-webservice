package com.paipeng.iot.controller;

import com.paipeng.iot.entity.User;
import com.paipeng.iot.repository.UserRepository;
import com.paipeng.iot.service.JwtService;
import com.paipeng.iot.service.TestService;
import org.antlr.v4.runtime.misc.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/tests")
public class TestController {
    private final Logger log = LogManager.getLogger(this.getClass().getName());


    @Autowired
    private TestService testService;


    @GetMapping(value = "/{username}", produces = {"application/json;charset=UTF-8"})
    public User test(@NotNull @PathVariable("username") String username) {
        log.trace("test");
        return testService.test(username);
    }
}
