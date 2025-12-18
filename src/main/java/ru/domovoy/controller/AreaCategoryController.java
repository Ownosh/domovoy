package ru.domovoy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.domovoy.model.AreaCategory;
import ru.domovoy.service.AreaCategoryService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/area-categories")
@CrossOrigin(origins = "*")
public class AreaCategoryController {
    private final AreaCategoryService categoryService;

    @Autowired
    public AreaCategoryController(AreaCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<AreaCategory>> getAllCategories() {
        List<AreaCategory> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaCategory> getCategoryById(@PathVariable @NonNull Long id) {
        Optional<AreaCategory> category = categoryService.getCategoryById(id);
        return category.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AreaCategory> createCategory(@RequestBody @NonNull AreaCategory category) {
        AreaCategory createdCategory = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaCategory> updateCategory(@PathVariable @NonNull Long id, @RequestBody @NonNull AreaCategory category) {
        try {
            AreaCategory updatedCategory = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(updatedCategory);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable @NonNull Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
