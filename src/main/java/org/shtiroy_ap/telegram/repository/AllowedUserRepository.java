package org.shtiroy_ap.telegram.repository;

import org.shtiroy_ap.telegram.entity.AllowedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllowedUserRepository extends JpaRepository<AllowedUser, Integer> {
    boolean existsByChatIdAndIsActiveTrue(Long chatId);
}
