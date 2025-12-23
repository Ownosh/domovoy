package ru.domovoy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.domovoy.model.UserNotification;

import java.util.List;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    List<UserNotification> findByUserUserId(Long userId);
    List<UserNotification> findByUserUserIdAndIsRead(Long userId, Boolean isRead);
    List<UserNotification> findByNotificationNotificationId(Long notificationId);
}








