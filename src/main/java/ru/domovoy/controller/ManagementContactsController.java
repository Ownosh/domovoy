package ru.domovoy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.domovoy.model.ManagementContacts;
import ru.domovoy.repository.ManagementContactsRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/management-contacts")
@CrossOrigin(origins = "*")
public class ManagementContactsController {
    private final ManagementContactsRepository contactsRepository;

    @Autowired
    public ManagementContactsController(ManagementContactsRepository contactsRepository) {
        this.contactsRepository = contactsRepository;
    }

    @GetMapping
    public ResponseEntity<List<ManagementContacts>> getAllContacts() {
        List<ManagementContacts> contacts = contactsRepository.findAll();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/complex/{complexId}")
    public ResponseEntity<ManagementContacts> getContactsByComplex(@PathVariable @NonNull Long complexId) {
        Optional<ManagementContacts> contacts = contactsRepository.findByComplexComplexId(complexId);
        return contacts.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagementContacts> getContactsById(@PathVariable @NonNull Long id) {
        Optional<ManagementContacts> contacts = contactsRepository.findById(id);
        return contacts.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ManagementContacts> createContacts(@RequestBody @NonNull ManagementContacts contacts) {
        ManagementContacts createdContacts = contactsRepository.save(contacts);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContacts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManagementContacts> updateContacts(@PathVariable @NonNull Long id, @RequestBody @NonNull ManagementContacts contacts) {
        return contactsRepository.findById(id)
                .map(existingContacts -> {
                    existingContacts.setCompanyName(contacts.getCompanyName());
                    existingContacts.setEmail(contacts.getEmail());
                    existingContacts.setPhone(contacts.getPhone());
                    existingContacts.setWebsite(contacts.getWebsite());
                    existingContacts.setAddress(contacts.getAddress());
                    existingContacts.setWorkingHours(contacts.getWorkingHours());
                    existingContacts.setEmergencyPhone(contacts.getEmergencyPhone());

                    ManagementContacts updatedContacts = contactsRepository.save(existingContacts);
                    return ResponseEntity.ok(updatedContacts);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContacts(@PathVariable @NonNull Long id) {
        if (!contactsRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        contactsRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}










