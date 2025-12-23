package ru.domovoy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.domovoy.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByUserUserId(Long userId);
    List<Request> findByStatus(Request.RequestStatus status);
    List<Request> findByCategory(Request.RequestCategory category);
    
    // Загружаем пользователя вместе с заявками
    @Query("SELECT r FROM Request r JOIN FETCH r.user")
    List<Request> findAllWithUser();
}












