package ru.domovoy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.domovoy.model.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findBySentByUserId(Long userId);
    List<Notification> findByType(Notification.NotificationType type);
}



