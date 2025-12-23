package ru.domovoy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.domovoy.model.BuildingPhoto;
import ru.domovoy.repository.BuildingPhotoRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/building-photos")
@CrossOrigin(origins = "*")
public class BuildingPhotoController {
    private final BuildingPhotoRepository photoRepository;

    @Autowired
    public BuildingPhotoController(BuildingPhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @GetMapping
    public ResponseEntity<List<BuildingPhoto>> getAllPhotos() {
        List<BuildingPhoto> photos = photoRepository.findAll();
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/building/{buildingId}")
    public ResponseEntity<List<BuildingPhoto>> getPhotosByBuilding(@PathVariable @NonNull Long buildingId) {
        List<BuildingPhoto> photos = photoRepository.findByBuildingBuildingId(buildingId);
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuildingPhoto> getPhotoById(@PathVariable @NonNull Long id) {
        Optional<BuildingPhoto> photo = photoRepository.findById(id);
        return photo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BuildingPhoto> createPhoto(@RequestBody @NonNull BuildingPhoto photo) {
        BuildingPhoto createdPhoto = photoRepository.save(photo);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPhoto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuildingPhoto> updatePhoto(@PathVariable @NonNull Long id, @RequestBody @NonNull BuildingPhoto photo) {
        return photoRepository.findById(id)
                .map(existingPhoto -> {
                    existingPhoto.setPhotoUrl(photo.getPhotoUrl());
                    existingPhoto.setDescription(photo.getDescription());

                    BuildingPhoto updatedPhoto = photoRepository.save(existingPhoto);
                    return ResponseEntity.ok(updatedPhoto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable @NonNull Long id) {
        if (!photoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        photoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}











