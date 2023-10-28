package com.paipeng.iot.repository;

import com.paipeng.iot.entity.ContactScanCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactScanCodeRepository extends JpaRepository<ContactScanCode, Long> {
    Optional<ContactScanCode> findByUuid(String uuid);
}
