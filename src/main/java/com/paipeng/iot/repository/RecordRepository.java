package com.paipeng.iot.repository;

import com.paipeng.iot.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface RecordRepository extends JpaRepository<Record, Long> {

    List<Record> findByDeviceId(Long deviceId);

    List<Record> findAllByDeviceId(Long id);
}