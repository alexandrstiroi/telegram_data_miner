package org.shtiroy_ap.telegram.repository;

import org.shtiroy_ap.telegram.entity.PinCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PinCodeRepository extends JpaRepository<PinCode, Long> {
    Optional<PinCode> findByCode(String code);
}
