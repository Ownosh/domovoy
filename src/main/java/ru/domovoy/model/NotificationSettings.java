package ru.domovoy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id")
    private Long settingId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "outage_enabled")
    private Boolean outageEnabled = true;

    @Column(name = "meeting_enabled")
    private Boolean meetingEnabled = true;

    @Column(name = "announcement_enabled")
    private Boolean announcementEnabled = true;

    @Column(name = "emergency_enabled")
    private Boolean emergencyEnabled = true;

    @Column(name = "general_enabled")
    private Boolean generalEnabled = true;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}











