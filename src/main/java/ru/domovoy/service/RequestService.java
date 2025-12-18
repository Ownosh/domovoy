package ru.domovoy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.domovoy.model.Request;
import ru.domovoy.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RequestService {
    private final RequestRepository requestRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public List<Request> getRequestsByUserId(Long userId) {
        return requestRepository.findByUserUserId(userId);
    }

    public List<Request> getRequestsByStatus(Request.RequestStatus status) {
        return requestRepository.findByStatus(status);
    }

    public Optional<Request> getRequestById(Long id) {
        return requestRepository.findById(id);
    }

    public Request createRequest(Request request) {
        return requestRepository.save(request);
    }

    public Request updateRequest(Long id, Request requestDetails) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));
        
        request.setCategory(requestDetails.getCategory());
        request.setSubject(requestDetails.getSubject());
        request.setDescription(requestDetails.getDescription());
        request.setStatus(requestDetails.getStatus());
        request.setPriority(requestDetails.getPriority());
        request.setRejectionReason(requestDetails.getRejectionReason());
        
        if (requestDetails.getStatus() == Request.RequestStatus.resolved && request.getResolvedAt() == null) {
            request.setResolvedAt(LocalDateTime.now());
        }
        
        return requestRepository.save(request);
    }

    public void deleteRequest(Long id) {
        requestRepository.deleteById(id);
    }
}

