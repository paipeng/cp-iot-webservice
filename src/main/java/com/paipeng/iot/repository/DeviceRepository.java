package com.paipeng.iot.repository;

import com.paipeng.iot.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByUdid(String udid);

    List<Device> findDevicesByUsersId(Long userId);

}
