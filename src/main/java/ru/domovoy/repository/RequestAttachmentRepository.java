package ru.domovoy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.domovoy.model.RequestAttachment;

import java.util.List;

@Repository
public interface RequestAttachmentRepository extends JpaRepository<RequestAttachment, Long> {
    List<RequestAttachment> findByRequestRequestId(Long requestId);
}



