package ru.domovoy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", columnDefinition = "ENUM('repair','utilities','cleaning','security','other')")
    private RequestCategory category;

    @Column(name = "subject", length = 255)
    private String subject;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status", columnDefinition = "ENUM('new','accepted','in_progress','resolved','rejected')")
    @Convert(converter = ru.domovoy.converter.RequestStatusConverter.class)
    private RequestStatus status = RequestStatus.new_;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", columnDefinition = "ENUM('low','medium','high','urgent')")
    private RequestPriority priority = RequestPriority.medium;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    // Relationships
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestAttachment> attachments;

    public enum RequestCategory {
        repair, utilities, cleaning, security, other
    }

    public enum RequestStatus {
        new_("new"), accepted("accepted"), in_progress("in_progress"), resolved("resolved"), rejected("rejected");
        
        private final String value;
        
        RequestStatus(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public static RequestStatus fromValue(String value) {
            for (RequestStatus status : RequestStatus.values()) {
                if (status.value.equals(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown status: " + value);
        }
    }

    public enum RequestPriority {
        low, medium, high, urgent
    }
}



