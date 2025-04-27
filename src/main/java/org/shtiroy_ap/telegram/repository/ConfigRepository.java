package org.shtiroy_ap.telegram.repository;

import org.shtiroy_ap.telegram.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Integer> {
    Optional<Config> findByParament(String parament);
}
