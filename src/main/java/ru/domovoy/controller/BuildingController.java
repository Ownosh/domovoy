package ru.domovoy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.domovoy.model.Building;
import ru.domovoy.repository.BuildingRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/buildings")
@CrossOrigin(origins = "*")
public class BuildingController {
    private final BuildingRepository buildingRepository;

    @Autowired
    public BuildingController(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    @GetMapping
    public ResponseEntity<List<Building>> getAllBuildings() {
        List<Building> buildings = buildingRepository.findAll();
        return ResponseEntity.ok(buildings);
    }

    @GetMapping("/complex/{complexId}")
    public ResponseEntity<List<Building>> getBuildingsByComplex(@PathVariable @NonNull Long complexId) {
        List<Building> buildings = buildingRepository.findByComplexComplexId(complexId);
        return ResponseEntity.ok(buildings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Building> getBuildingById(@PathVariable @NonNull Long id) {
        Optional<Building> building = buildingRepository.findById(id);
        return building.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Building> createBuilding(@RequestBody @NonNull Building building) {
        Building createdBuilding = buildingRepository.save(building);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBuilding);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Building> updateBuilding(@PathVariable @NonNull Long id, @RequestBody @NonNull Building building) {
        return buildingRepository.findById(id)
                .map(existingBuilding -> {
                    existingBuilding.setBuildingNumber(building.getBuildingNumber());
                    existingBuilding.setFloors(building.getFloors());
                    existingBuilding.setTotalApartments(building.getTotalApartments());
                    existingBuilding.setYearBuilt(building.getYearBuilt());
                    existingBuilding.setConstructionType(building.getConstructionType());
                    existingBuilding.setTechnicalInfo(building.getTechnicalInfo());

                    if (building.getComplex() != null) {
                        existingBuilding.setComplex(building.getComplex());
                    }

                    Building updatedBuilding = buildingRepository.save(existingBuilding);
                    return ResponseEntity.ok(updatedBuilding);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable @NonNull Long id) {
        if (!buildingRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        buildingRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}











