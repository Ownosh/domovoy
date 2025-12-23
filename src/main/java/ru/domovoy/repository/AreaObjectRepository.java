package ru.domovoy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.domovoy.model.AreaObject;

import java.util.List;

@Repository
public interface AreaObjectRepository extends JpaRepository<AreaObject, Long> {
    List<AreaObject> findByCategoryCategoryId(Long categoryId);
}








