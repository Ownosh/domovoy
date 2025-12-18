package ru.domovoy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.domovoy.model.Request;
import ru.domovoy.service.RequestService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/requests")
@CrossOrigin(origins = "*")
public class RequestController {
    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public ResponseEntity<List<Request>> getAllRequests() {
        List<Request> requests = requestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Request>> getRequestsByUser(@PathVariable @NonNull Long userId) {
        List<Request> requests = requestService.getRequestsByUserId(userId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Request>> getRequestsByStatus(@PathVariable @NonNull String status) {
        try {
            Request.RequestStatus requestStatus = Request.RequestStatus.valueOf(status);
            List<Request> requests = requestService.getRequestsByStatus(requestStatus);
            return ResponseEntity.ok(requests);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Request> getRequestById(@PathVariable @NonNull Long id) {
        Optional<Request> request = requestService.getRequestById(id);
        return request.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Request> createRequest(@RequestBody @NonNull Request request) {
        Request createdRequest = requestService.createRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Request> updateRequest(@PathVariable @NonNull Long id, @RequestBody @NonNull Request request) {
        try {
            Request updatedRequest = requestService.updateRequest(id, request);
            return ResponseEntity.ok(updatedRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable @NonNull Long id) {
        try {
            requestService.deleteRequest(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}



