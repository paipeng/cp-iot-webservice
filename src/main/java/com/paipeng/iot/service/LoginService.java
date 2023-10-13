package com.paipeng.iot.service;

import com.paipeng.iot.entity.Role;
import com.paipeng.iot.entity.User;
import com.paipeng.iot.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public User register(User user) throws Exception {
        User existUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (existUser == null) {
            logger.info("username does not exist!");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(Role.USER);
            user = userRepository.saveAndFlush(user);
            var jwtToken = jwtService.generateToken(user);
            user.setToken(jwtToken);
            return user;
        } else {
            throw new Exception();
        }
    }

    public User login(User user) throws Exception {
        logger.info("login: " + user.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                )
        );
        User existUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (existUser != null) {
            var jwtToken = jwtService.generateToken(existUser);
            existUser.setToken(jwtToken);
            return existUser;
        } else {
            throw new Exception();
        }
    }
}
