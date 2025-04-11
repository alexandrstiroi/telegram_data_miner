package org.shtiroy_ap.telegram.repository;

import org.shtiroy_ap.telegram.entity.UserRetries;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRetriesRepository extends JpaRepository<UserRetries, Integer> {
    Optional<UserRetries> findByChatId(Long chatId);
}
