package com.paipeng.iot.controller;

import com.paipeng.iot.entity.User;
import com.paipeng.iot.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/login")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping(value = "/register", produces = {"application/json;charset=UTF-8"})
    public User register(@RequestBody User user, HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setStatus(HttpStatus.CREATED.value());
        return loginService.register(user);
    }

    @PostMapping(value = "", produces = {"application/json;charset=UTF-8"})
    public User login(@RequestBody User user, HttpServletResponse httpServletResponse) throws Exception {
        return loginService.login(user);
    }
}
