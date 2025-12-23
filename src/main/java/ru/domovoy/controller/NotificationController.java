package ru.domovoy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.domovoy.model.Notification;
import ru.domovoy.model.UserNotification;
import ru.domovoy.service.NotificationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable @NonNull Long id) {
        Optional<Notification> notification = notificationService.getNotificationById(id);
        return notification.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody @NonNull Notification notification) {
        Notification createdNotification = notificationService.createNotification(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNotification);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notification> updateNotification(@PathVariable @NonNull Long id, @RequestBody @NonNull Notification notification) {
        try {
            Notification updatedNotification = notificationService.updateNotification(id, notification);
            return ResponseEntity.ok(updatedNotification);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable @NonNull Long id) {
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserNotification>> getUserNotifications(@PathVariable @NonNull Long userId) {
        List<UserNotification> userNotifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(userNotifications);
    }

    @PutMapping("/user-notifications/{id}/read")
    public ResponseEntity<UserNotification> markAsRead(@PathVariable @NonNull Long id) {
        try {
            UserNotification userNotification = notificationService.markAsRead(id);
            return ResponseEntity.ok(userNotification);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}








