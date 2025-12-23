package ru.domovoy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.domovoy.model.UserVerification;

import java.util.List;

@Repository
public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {
    List<UserVerification> findByUserUserId(Long userId);
    List<UserVerification> findByStatus(UserVerification.VerificationStatus status);
    List<UserVerification> findByBuildingBuildingId(Long buildingId);
    
    // Загружаем пользователя и здание вместе с верификациями
    @Query("SELECT v FROM UserVerification v JOIN FETCH v.user LEFT JOIN FETCH v.building")
    List<UserVerification> findAllWithUserAndBuilding();
}












