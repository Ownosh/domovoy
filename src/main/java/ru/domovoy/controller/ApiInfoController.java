package ru.domovoy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class ApiInfoController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        Map<String, Object> apiInfo = new HashMap<>();
        apiInfo.put("name", "Domovoy API");
        apiInfo.put("version", "1.0.0");
        apiInfo.put("description", "REST API для системы управления домом");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("users", "/api/users");
        endpoints.put("residentialComplexes", "/api/complexes");
        endpoints.put("buildings", "/api/buildings");
        endpoints.put("buildingPhotos", "/api/building-photos");
        endpoints.put("news", "/api/news");
        endpoints.put("requests", "/api/requests");
        endpoints.put("requestAttachments", "/api/request-attachments");
        endpoints.put("notifications", "/api/notifications");
        endpoints.put("notificationSettings", "/api/notification-settings");
        endpoints.put("areaCategories", "/api/area-categories");
        endpoints.put("areaObjects", "/api/area-objects");
        endpoints.put("userVerifications", "/api/user-verifications");
        endpoints.put("deviceTokens", "/api/device-tokens");
        endpoints.put("managementContacts", "/api/management-contacts");
        
        apiInfo.put("endpoints", endpoints);
        apiInfo.put("example", "Попробуйте: GET /api/users для получения списка пользователей");
        
        return ResponseEntity.ok(apiInfo);
    }
}








