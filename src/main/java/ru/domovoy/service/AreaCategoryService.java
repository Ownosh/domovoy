package ru.domovoy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.domovoy.model.AreaCategory;
import ru.domovoy.repository.AreaCategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AreaCategoryService {
    private final AreaCategoryRepository categoryRepository;

    @Autowired
    public AreaCategoryService(AreaCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<AreaCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<AreaCategory> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public AreaCategory createCategory(AreaCategory category) {
        return categoryRepository.save(category);
    }

    public AreaCategory updateCategory(Long id, AreaCategory categoryDetails) {
        AreaCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Area category not found with id: " + id));
        
        category.setName(categoryDetails.getName());
        category.setIconName(categoryDetails.getIconName());
        category.setDescription(categoryDetails.getDescription());
        
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}








