package ru.domovoy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.domovoy.model.RequestAttachment;
import ru.domovoy.repository.RequestAttachmentRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/request-attachments")
@CrossOrigin(origins = "*")
public class RequestAttachmentController {
    private final RequestAttachmentRepository attachmentRepository;

    @Autowired
    public RequestAttachmentController(RequestAttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @GetMapping
    public ResponseEntity<List<RequestAttachment>> getAllAttachments() {
        List<RequestAttachment> attachments = attachmentRepository.findAll();
        return ResponseEntity.ok(attachments);
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<List<RequestAttachment>> getAttachmentsByRequest(@PathVariable @NonNull Long requestId) {
        List<RequestAttachment> attachments = attachmentRepository.findByRequestRequestId(requestId);
        return ResponseEntity.ok(attachments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestAttachment> getAttachmentById(@PathVariable @NonNull Long id) {
        Optional<RequestAttachment> attachment = attachmentRepository.findById(id);
        return attachment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RequestAttachment> createAttachment(@RequestBody @NonNull RequestAttachment attachment) {
        RequestAttachment createdAttachment = attachmentRepository.save(attachment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAttachment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequestAttachment> updateAttachment(@PathVariable @NonNull Long id, @RequestBody @NonNull RequestAttachment attachment) {
        return attachmentRepository.findById(id)
                .map(existingAttachment -> {
                    existingAttachment.setFileUrl(attachment.getFileUrl());
                    existingAttachment.setFileType(attachment.getFileType());

                    RequestAttachment updatedAttachment = attachmentRepository.save(existingAttachment);
                    return ResponseEntity.ok(updatedAttachment);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable @NonNull Long id) {
        if (!attachmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        attachmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}










