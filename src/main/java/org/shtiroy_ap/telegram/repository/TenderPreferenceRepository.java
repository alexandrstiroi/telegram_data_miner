package org.shtiroy_ap.telegram.repository;

import org.shtiroy_ap.telegram.entity.TenderPreference;
import org.shtiroy_ap.telegram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TenderPreferenceRepository extends JpaRepository<TenderPreference, Integer> {
    List<TenderPreference> findByUser(User user);

    boolean existsByUserAndCategoryId(User user, String categoryId);

    @Transactional
    void deleteByUserAndCategoryId(User user, String categoryId);
}
