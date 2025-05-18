package org.shtiroy_ap.telegram.repository;

import org.shtiroy_ap.telegram.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByParentId(Integer parentId);

    Optional<Category> findByCode(String code);
}
