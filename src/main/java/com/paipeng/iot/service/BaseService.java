package com.paipeng.iot.service;

import com.paipeng.iot.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public abstract class BaseService {

    protected User getUserFromSecurity() {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            User user = (User)auth.getPrincipal();
            user.setDevices(null);
            return  user;
        } else {
            return null;
        }
    }
}
