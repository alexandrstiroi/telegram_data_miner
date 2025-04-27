package org.shtiroy_ap.telegram.repository;

import org.shtiroy_ap.telegram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
