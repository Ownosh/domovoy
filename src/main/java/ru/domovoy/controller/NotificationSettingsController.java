package ru.domovoy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.domovoy.model.NotificationSettings;
import ru.domovoy.repository.NotificationSettingsRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notification-settings")
@CrossOrigin(origins = "*")
public class NotificationSettingsController {
    private final NotificationSettingsRepository settingsRepository;

    @Autowired
    public NotificationSettingsController(NotificationSettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @GetMapping
    public ResponseEntity<List<NotificationSettings>> getAllSettings() {
        List<NotificationSettings> settings = settingsRepository.findAll();
        return ResponseEntity.ok(settings);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<NotificationSettings> getSettingsByUser(@PathVariable @NonNull Long userId) {
        Optional<NotificationSettings> settings = settingsRepository.findByUserUserId(userId);
        return settings.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationSettings> getSettingsById(@PathVariable @NonNull Long id) {
        Optional<NotificationSettings> settings = settingsRepository.findById(id);
        return settings.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NotificationSettings> createSettings(@RequestBody @NonNull NotificationSettings settings) {
        NotificationSettings createdSettings = settingsRepository.save(settings);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSettings);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationSettings> updateSettings(@PathVariable @NonNull Long id, @RequestBody @NonNull NotificationSettings settings) {
        return settingsRepository.findById(id)
                .map(existingSettings -> {
                    existingSettings.setOutageEnabled(settings.getOutageEnabled());
                    existingSettings.setMeetingEnabled(settings.getMeetingEnabled());
                    existingSettings.setAnnouncementEnabled(settings.getAnnouncementEnabled());
                    existingSettings.setEmergencyEnabled(settings.getEmergencyEnabled());
                    existingSettings.setGeneralEnabled(settings.getGeneralEnabled());

                    NotificationSettings updatedSettings = settingsRepository.save(existingSettings);
                    return ResponseEntity.ok(updatedSettings);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSettings(@PathVariable @NonNull Long id) {
        if (!settingsRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        settingsRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}











