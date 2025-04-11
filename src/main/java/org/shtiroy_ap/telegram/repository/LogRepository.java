package org.shtiroy_ap.telegram.repository;

import org.shtiroy_ap.telegram.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Integer> {
}
