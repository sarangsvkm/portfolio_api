package com.sarangsvkm.portfolio_api.controller;

import com.sarangsvkm.portfolio_api.entity.ContactRequest;
import com.sarangsvkm.portfolio_api.service.ContactRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contact")
public class ContactRequestController {

    private final ContactRequestService service;

    public ContactRequestController(ContactRequestService service) {
        this.service = service;
    }

    // ✅ PUBLIC
    @PostMapping("/request-otp")
    public Map<String, String> requestOtp(@RequestBody ContactRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        if (request.getPhone() == null || request.getPhone().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is required");
        }
        service.generateAndSaveOtp(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "An OTP has been sent to your email address!");
        return response;
    }

    // ✅ PUBLIC
    @PostMapping("/verify-otp")
    public Map<String, String> verifyOtp(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String otp = payload.get("otp");
        try {
            String phoneNumber = service.verifyOtp(email, otp);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Verification successful");
            response.put("phone", phoneNumber);
            return response;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    // ✅ PROTECTED (Auth by Filter)
    @GetMapping("/report")
    public List<Map<String, Object>> getContactRequestReport() {
        return service.getAll().stream().map(cr -> {
            Map<String, Object> row = new HashMap<>();
            row.put("id",        cr.getId());
            row.put("name",      cr.getName());
            row.put("email",     cr.getEmail());
            row.put("phone",     cr.getPhone());
            row.put("verified",  cr.isVerified());
            row.put("createdAt", cr.getCreatedAt() != null ? cr.getCreatedAt().toString() : null);
            return row;
        }).collect(Collectors.toList());
    }

    // ✅ PROTECTED (Auth by Filter)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
