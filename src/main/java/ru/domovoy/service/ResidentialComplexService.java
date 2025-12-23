package ru.domovoy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.domovoy.model.ResidentialComplex;
import ru.domovoy.repository.ResidentialComplexRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ResidentialComplexService {
    private final ResidentialComplexRepository complexRepository;

    @Autowired
    public ResidentialComplexService(ResidentialComplexRepository complexRepository) {
        this.complexRepository = complexRepository;
    }

    public List<ResidentialComplex> getAllComplexes() {
        return complexRepository.findAll();
    }

    public Optional<ResidentialComplex> getComplexById(Long id) {
        return complexRepository.findById(id);
    }

    public ResidentialComplex createComplex(ResidentialComplex complex) {
        return complexRepository.save(complex);
    }

    public ResidentialComplex updateComplex(Long id, ResidentialComplex complexDetails) {
        ResidentialComplex complex = complexRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Residential complex not found with id: " + id));
        
        complex.setName(complexDetails.getName());
        complex.setAddress(complexDetails.getAddress());
        complex.setDescription(complexDetails.getDescription());
        
        return complexRepository.save(complex);
    }

    public void deleteComplex(Long id) {
        complexRepository.deleteById(id);
    }
}










