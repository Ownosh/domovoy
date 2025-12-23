package ru.domovoy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.domovoy.model.UserVerification;

import java.util.List;

@Repository
public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {
    List<UserVerification> findByUserUserId(Long userId);
    List<UserVerification> findByStatus(UserVerification.VerificationStatus status);
    List<UserVerification> findByBuildingBuildingId(Long buildingId);
}








