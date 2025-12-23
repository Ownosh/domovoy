package ru.domovoy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.domovoy.model.Notification;
import ru.domovoy.model.UserNotification;
import ru.domovoy.repository.NotificationRepository;
import ru.domovoy.repository.UserNotificationRepository;
import ru.domovoy.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final UserRepository userRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                              UserNotificationRepository userNotificationRepository,
                              UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userNotificationRepository = userNotificationRepository;
        this.userRepository = userRepository;
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Notification updateNotification(Long id, Notification notificationDetails) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        
        notification.setTitle(notificationDetails.getTitle());
        notification.setMessage(notificationDetails.getMessage());
        notification.setType(notificationDetails.getType());
        notification.setTargetAudience(notificationDetails.getTargetAudience());
        
        return notificationRepository.save(notification);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    public List<UserNotification> getUserNotifications(Long userId) {
        return userNotificationRepository.findByUserUserId(userId);
    }

    public UserNotification markAsRead(Long userNotificationId) {
        UserNotification userNotification = userNotificationRepository.findById(userNotificationId)
                .orElseThrow(() -> new RuntimeException("User notification not found with id: " + userNotificationId));
        
        userNotification.setIsRead(true);
        userNotification.setReadAt(LocalDateTime.now());
        
        return userNotificationRepository.save(userNotification);
    }
}










