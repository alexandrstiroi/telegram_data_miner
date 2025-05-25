package org.shtiroy_ap.telegram.repository;

import org.shtiroy_ap.telegram.entity.UserFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Integer> {

    boolean existsByChatIdAndTenderId(Long chatId, String tenderId);
}
