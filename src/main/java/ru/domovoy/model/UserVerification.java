package ru.domovoy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_verification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verification_id")
    private Long verificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "passwordHash", "deviceTokens", "notificationSettings", "userNotifications", "sentNotifications", "news", "requests", "userVerifications", "reviewedVerifications"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "complex", "buildingPhotos", "userVerifications"})
    private Building building;

    @Column(name = "apartment_number", length = 20)
    private String apartmentNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", columnDefinition = "ENUM('ownership','rental')")
    private DocumentType documentType;

    @Column(name = "document_url", length = 500)
    private String documentUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('pending','approved','rejected')")
    private VerificationStatus status = VerificationStatus.pending;

    @CreationTimestamp
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "passwordHash", "deviceTokens", "notificationSettings", "userNotifications", "sentNotifications", "news", "requests", "userVerifications", "reviewedVerifications"})
    private User reviewedBy;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    public enum DocumentType {
        ownership, rental
    }

    public enum VerificationStatus {
        pending, approved, rejected
    }
}











