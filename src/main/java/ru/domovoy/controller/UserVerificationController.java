package ru.domovoy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.domovoy.model.UserVerification;
import ru.domovoy.repository.UserVerificationRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user-verifications")
@CrossOrigin(origins = "*")
public class UserVerificationController {
    private final UserVerificationRepository verificationRepository;

    @Autowired
    public UserVerificationController(UserVerificationRepository verificationRepository) {
        this.verificationRepository = verificationRepository;
    }

    @GetMapping
    public ResponseEntity<List<UserVerification>> getAllVerifications() {
        List<UserVerification> verifications = verificationRepository.findAll();
        return ResponseEntity.ok(verifications);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserVerification>> getVerificationsByUser(@PathVariable @NonNull Long userId) {
        List<UserVerification> verifications = verificationRepository.findByUserUserId(userId);
        return ResponseEntity.ok(verifications);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<UserVerification>> getVerificationsByStatus(@PathVariable @NonNull String status) {
        try {
            UserVerification.VerificationStatus verificationStatus = UserVerification.VerificationStatus.valueOf(status);
            List<UserVerification> verifications = verificationRepository.findByStatus(verificationStatus);
            return ResponseEntity.ok(verifications);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserVerification> getVerificationById(@PathVariable @NonNull Long id) {
        Optional<UserVerification> verification = verificationRepository.findById(id);
        return verification.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserVerification> createVerification(@RequestBody @NonNull UserVerification verification) {
        UserVerification createdVerification = verificationRepository.save(verification);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVerification);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserVerification> updateVerification(@PathVariable @NonNull Long id, @RequestBody @NonNull UserVerification verification) {
        return verificationRepository.findById(id)
                .map(existingVerification -> {
                    existingVerification.setApartmentNumber(verification.getApartmentNumber());
                    existingVerification.setDocumentType(verification.getDocumentType());
                    existingVerification.setDocumentUrl(verification.getDocumentUrl());
                    existingVerification.setStatus(verification.getStatus());
                    existingVerification.setRejectionReason(verification.getRejectionReason());
                    existingVerification.setReviewedBy(verification.getReviewedBy());

                    if (verification.getReviewedAt() != null) {
                        existingVerification.setReviewedAt(verification.getReviewedAt());
                    }

                    UserVerification updatedVerification = verificationRepository.save(existingVerification);
                    return ResponseEntity.ok(updatedVerification);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVerification(@PathVariable @NonNull Long id) {
        if (!verificationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        verificationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

