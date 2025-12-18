package ru.domovoy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.domovoy.model.ResidentialComplex;

@Repository
public interface ResidentialComplexRepository extends JpaRepository<ResidentialComplex, Long> {
}



