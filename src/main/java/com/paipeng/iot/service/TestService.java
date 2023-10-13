package com.paipeng.iot.service;

import com.paipeng.iot.entity.User;
import com.paipeng.iot.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    public User test(String username) {
        logger.info("test: " + username);
        User user = userRepository.findByEmail(username).orElse(null);
        if (user != null) {
            var jwtToken = jwtService.generateToken(user);
            user.setToken(jwtToken);
        } else {
            logger.error("user not found!");
        }
        return user;
    }
}
