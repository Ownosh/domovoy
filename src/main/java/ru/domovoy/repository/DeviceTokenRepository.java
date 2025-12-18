package ru.domovoy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.domovoy.model.DeviceToken;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    List<DeviceToken> findByUserUserId(Long userId);
    Optional<DeviceToken> findByDeviceToken(String deviceToken);
}



