package ru.domovoy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.domovoy.model.DeviceToken;
import ru.domovoy.repository.DeviceTokenRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/device-tokens")
@CrossOrigin(origins = "*")
public class DeviceTokenController {
    private final DeviceTokenRepository deviceTokenRepository;

    @Autowired
    public DeviceTokenController(DeviceTokenRepository deviceTokenRepository) {
        this.deviceTokenRepository = deviceTokenRepository;
    }

    @GetMapping
    public ResponseEntity<List<DeviceToken>> getAllTokens() {
        List<DeviceToken> tokens = deviceTokenRepository.findAll();
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DeviceToken>> getTokensByUser(@PathVariable @NonNull Long userId) {
        List<DeviceToken> tokens = deviceTokenRepository.findByUserUserId(userId);
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceToken> getTokenById(@PathVariable @NonNull Long id) {
        Optional<DeviceToken> token = deviceTokenRepository.findById(id);
        return token.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DeviceToken> createToken(@RequestBody @NonNull DeviceToken deviceToken) {
        DeviceToken createdToken = deviceTokenRepository.save(deviceToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdToken);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceToken> updateToken(@PathVariable @NonNull Long id, @RequestBody @NonNull DeviceToken deviceToken) {
        return deviceTokenRepository.findById(id)
                .map(existingToken -> {
                    existingToken.setDeviceToken(deviceToken.getDeviceToken());
                    existingToken.setDeviceType(deviceToken.getDeviceType());
                    existingToken.setLastUsedAt(deviceToken.getLastUsedAt());

                    DeviceToken updatedToken = deviceTokenRepository.save(existingToken);
                    return ResponseEntity.ok(updatedToken);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToken(@PathVariable @NonNull Long id) {
        if (!deviceTokenRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        deviceTokenRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}











