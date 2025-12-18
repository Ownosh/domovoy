package ru.domovoy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.domovoy.model.ResidentialComplex;
import ru.domovoy.service.ResidentialComplexService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/complexes")
@CrossOrigin(origins = "*")
public class ResidentialComplexController {
    private final ResidentialComplexService complexService;

    @Autowired
    public ResidentialComplexController(ResidentialComplexService complexService) {
        this.complexService = complexService;
    }

    @GetMapping
    public ResponseEntity<List<ResidentialComplex>> getAllComplexes() {
        List<ResidentialComplex> complexes = complexService.getAllComplexes();
        return ResponseEntity.ok(complexes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResidentialComplex> getComplexById(@PathVariable @NonNull Long id) {
        Optional<ResidentialComplex> complex = complexService.getComplexById(id);
        return complex.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ResidentialComplex> createComplex(@RequestBody @NonNull ResidentialComplex complex) {
        ResidentialComplex createdComplex = complexService.createComplex(complex);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComplex);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResidentialComplex> updateComplex(@PathVariable @NonNull Long id, @RequestBody @NonNull ResidentialComplex complex) {
        try {
            ResidentialComplex updatedComplex = complexService.updateComplex(id, complex);
            return ResponseEntity.ok(updatedComplex);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComplex(@PathVariable @NonNull Long id) {
        try {
            complexService.deleteComplex(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

