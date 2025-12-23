package ru.domovoy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.domovoy.model.AreaObject;
import ru.domovoy.repository.AreaObjectRepository;
import ru.domovoy.repository.AreaCategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AreaObjectService {
    private final AreaObjectRepository areaObjectRepository;
    private final AreaCategoryRepository categoryRepository;

    @Autowired
    public AreaObjectService(AreaObjectRepository areaObjectRepository,
                            AreaCategoryRepository categoryRepository) {
        this.areaObjectRepository = areaObjectRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<AreaObject> getAllObjects() {
        return areaObjectRepository.findAll();
    }

    public List<AreaObject> getObjectsByCategory(Long categoryId) {
        return areaObjectRepository.findByCategoryCategoryId(categoryId);
    }

    public Optional<AreaObject> getObjectById(Long id) {
        return areaObjectRepository.findById(id);
    }

    public AreaObject createObject(AreaObject areaObject) {
        return areaObjectRepository.save(areaObject);
    }

    public AreaObject updateObject(Long id, AreaObject objectDetails) {
        AreaObject areaObject = areaObjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Area object not found with id: " + id));
        
        areaObject.setName(objectDetails.getName());
        areaObject.setAddress(objectDetails.getAddress());
        areaObject.setLatitude(objectDetails.getLatitude());
        areaObject.setLongitude(objectDetails.getLongitude());
        areaObject.setDescription(objectDetails.getDescription());
        areaObject.setPhone(objectDetails.getPhone());
        areaObject.setWebsite(objectDetails.getWebsite());
        areaObject.setWorkingHours(objectDetails.getWorkingHours());
        
        if (objectDetails.getCategory() != null) {
            areaObject.setCategory(objectDetails.getCategory());
        }
        
        return areaObjectRepository.save(areaObject);
    }

    public void deleteObject(Long id) {
        areaObjectRepository.deleteById(id);
    }
}











