package ru.domovoy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.domovoy.model.BuildingPhoto;

import java.util.List;

@Repository
public interface BuildingPhotoRepository extends JpaRepository<BuildingPhoto, Long> {
    List<BuildingPhoto> findByBuildingBuildingId(Long buildingId);
}



