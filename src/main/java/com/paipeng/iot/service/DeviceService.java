package com.paipeng.iot.service;

import com.paipeng.iot.entity.Device;
import com.paipeng.iot.entity.User;
import com.paipeng.iot.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;
    public List<Device> get() {
        return deviceRepository.findAll();
    }

    public Device getById(Long id) {
        return deviceRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }

    public Device save(Device device) {
        // set user
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        User user = (User)usernamePasswordAuthenticationToken.getPrincipal();

        device.setUsers(List.of(user));
        device = deviceRepository.saveAndFlush(device);
        return device;
    }

    public Device update(Device device) {
        device = deviceRepository.saveAndFlush(device);
        return device;
    }

    public void delete(Long id) {
        Device device = deviceRepository.findById(id).orElseThrow(() -> new RuntimeException());
        deviceRepository.delete(device);
    }
}
