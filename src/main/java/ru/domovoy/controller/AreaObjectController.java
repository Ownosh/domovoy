package ru.domovoy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.domovoy.model.AreaObject;
import ru.domovoy.service.AreaObjectService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/area-objects")
@CrossOrigin(origins = "*")
public class AreaObjectController {
    private final AreaObjectService areaObjectService;

    @Autowired
    public AreaObjectController(AreaObjectService areaObjectService) {
        this.areaObjectService = areaObjectService;
    }

    @GetMapping
    public ResponseEntity<List<AreaObject>> getAllObjects() {
        List<AreaObject> objects = areaObjectService.getAllObjects();
        return ResponseEntity.ok(objects);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<AreaObject>> getObjectsByCategory(@PathVariable @NonNull Long categoryId) {
        List<AreaObject> objects = areaObjectService.getObjectsByCategory(categoryId);
        return ResponseEntity.ok(objects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaObject> getObjectById(@PathVariable @NonNull Long id) {
        Optional<AreaObject> areaObject = areaObjectService.getObjectById(id);
        return areaObject.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AreaObject> createObject(@RequestBody @NonNull AreaObject areaObject) {
        AreaObject createdObject = areaObjectService.createObject(areaObject);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdObject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaObject> updateObject(@PathVariable @NonNull Long id, @RequestBody @NonNull AreaObject areaObject) {
        try {
            AreaObject updatedObject = areaObjectService.updateObject(id, areaObject);
            return ResponseEntity.ok(updatedObject);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteObject(@PathVariable @NonNull Long id) {
        try {
            areaObjectService.deleteObject(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
