package ru.domovoy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.domovoy.model.ManagementContacts;

import java.util.Optional;

@Repository
public interface ManagementContactsRepository extends JpaRepository<ManagementContacts, Long> {
    Optional<ManagementContacts> findByComplexComplexId(Long complexId);
}










