package ru.domovoy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.domovoy.model.AreaCategory;

@Repository
public interface AreaCategoryRepository extends JpaRepository<AreaCategory, Long> {
}








