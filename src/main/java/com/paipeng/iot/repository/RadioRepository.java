package com.paipeng.iot.repository;

import com.paipeng.iot.entity.Radio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RadioRepository extends JpaRepository<Radio, Long> {
    Optional<Radio> findByName(String name);

    @Query(value = "SELECT * FROM radio r WHERE r.valid = b'1'", nativeQuery=true)
    List<Radio> findRadiosByValid();
}
