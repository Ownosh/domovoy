package ru.domovoy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "ENUM('outage','meeting','announcement','emergency','general')")
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_audience", columnDefinition = "ENUM('all','verified','specific')")
    private TargetAudience targetAudience;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sent_by")
    private User sentBy;

    @CreationTimestamp
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    // Relationships
    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserNotification> userNotifications;

    public enum NotificationType {
        outage, meeting, announcement, emergency, general
    }

    public enum TargetAudience {
        all, verified, specific
    }
}

