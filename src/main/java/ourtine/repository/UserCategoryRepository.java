package ourtine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ourtine.domain.mapping.UserCategory;

import java.util.List;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
    List<UserCategory> findByUserId(Long userId);
}
