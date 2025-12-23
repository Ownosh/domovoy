package ru.domovoy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.domovoy.model.User;
import ru.domovoy.model.UserVerification;
import ru.domovoy.repository.UserRepository;
import ru.domovoy.repository.UserVerificationRepository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user-verifications")
@CrossOrigin(origins = "*")
public class UserVerificationController {
    private final UserVerificationRepository verificationRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserVerificationController(UserVerificationRepository verificationRepository, UserRepository userRepository) {
        this.verificationRepository = verificationRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<UserVerification>> getAllVerifications() {
        List<UserVerification> verifications = verificationRepository.findAllWithUserAndBuilding();
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
        Optional<UserVerification> verification = verificationRepository.findByIdWithUserAndBuilding(id);
        return verification.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserVerification> createVerification(@RequestBody @NonNull UserVerification verification) {
        UserVerification createdVerification = verificationRepository.save(verification);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVerification);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserVerification> updateVerification(@PathVariable @NonNull Long id, @RequestBody @NonNull Map<String, Object> updates) {
        return verificationRepository.findByIdWithUserAndBuilding(id)
                .map(existingVerification -> {
                    // Обновляем только переданные поля
                    if (updates.containsKey("apartmentNumber")) {
                        existingVerification.setApartmentNumber((String) updates.get("apartmentNumber"));
                    }
                    if (updates.containsKey("documentType")) {
                        String documentTypeStr = (String) updates.get("documentType");
                        if (documentTypeStr != null) {
                            existingVerification.setDocumentType(UserVerification.DocumentType.valueOf(documentTypeStr));
                        }
                    }
                    if (updates.containsKey("documentUrl")) {
                        existingVerification.setDocumentUrl((String) updates.get("documentUrl"));
                    }
                    if (updates.containsKey("status")) {
                        String statusStr = (String) updates.get("status");
                        if (statusStr != null) {
                            existingVerification.setStatus(UserVerification.VerificationStatus.valueOf(statusStr));
                        }
                    }
                    if (updates.containsKey("rejectionReason")) {
                        existingVerification.setRejectionReason((String) updates.get("rejectionReason"));
                    }
                    
                    // Обновляем reviewedBy если передан
                    if (updates.containsKey("reviewedBy")) {
                        Object reviewedByObj = updates.get("reviewedBy");
                        if (reviewedByObj != null) {
                            Long reviewerId = extractUserId(reviewedByObj);
                            if (reviewerId != null) {
                                final Long finalReviewerId = reviewerId;
                                User reviewer = userRepository.findById(finalReviewerId)
                                        .orElseThrow(() -> new RuntimeException("User not found with id: " + finalReviewerId));
                                existingVerification.setReviewedBy(reviewer);
                            }
                        }
                    }

                    // Устанавливаем reviewedAt если передан или если статус меняется на approved/rejected
                    if (updates.containsKey("reviewedAt")) {
                        Object reviewedAtObj = updates.get("reviewedAt");
                        if (reviewedAtObj != null) {
                            if (reviewedAtObj instanceof String) {
                                try {
                                    String dateStr = (String) reviewedAtObj;
                                    // Парсим ISO 8601 формат
                                    OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateStr);
                                    existingVerification.setReviewedAt(offsetDateTime.toLocalDateTime());
                                } catch (Exception e) {
                                    // Если не удалось распарсить, используем текущее время
                                    existingVerification.setReviewedAt(LocalDateTime.now());
                                }
                            }
                        }
                    } else if (updates.containsKey("status")) {
                        String statusStr = (String) updates.get("status");
                        if (statusStr != null && 
                            (statusStr.equals("approved") || statusStr.equals("rejected"))) {
                            existingVerification.setReviewedAt(LocalDateTime.now());
                        }
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
    
    private Long extractUserId(Object reviewedByObj) {
        if (reviewedByObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> reviewedByMap = (Map<String, Object>) reviewedByObj;
            Object userIdObj = reviewedByMap.get("userId");
            if (userIdObj instanceof Number) {
                return ((Number) userIdObj).longValue();
            } else if (userIdObj instanceof String) {
                return Long.parseLong((String) userIdObj);
            }
        } else if (reviewedByObj instanceof Number) {
            return ((Number) reviewedByObj).longValue();
        } else if (reviewedByObj instanceof String) {
            return Long.parseLong((String) reviewedByObj);
        }
        return null;
    }
}












