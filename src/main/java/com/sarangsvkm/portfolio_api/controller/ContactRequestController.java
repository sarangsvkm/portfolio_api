package com.sarangsvkm.portfolio_api.controller;

import com.sarangsvkm.portfolio_api.entity.ContactRequest;
import com.sarangsvkm.portfolio_api.service.ContactRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
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
            Map<String, String> details = service.verifyOtp(email, otp);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Verification successful");
            response.putAll(details);
            return response;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/report")
    public List<Map<String, Object>> getContactRequestReport() {
        DateTimeFormatter dateDoc = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeDoc = DateTimeFormatter.ofPattern("HH:mm:ss");

        return service.getAll().stream().map(cr -> {
            Map<String, Object> row = new HashMap<>();
            row.put("id",        cr.getId());
            row.put("name",      cr.getName());
            row.put("email",     cr.getEmail());
            row.put("phone",     cr.getPhone());
            row.put("verified",  cr.isVerified());
            
            if (cr.getCreatedAt() != null) {
                row.put("date", cr.getCreatedAt().format(dateDoc));
                row.put("time", cr.getCreatedAt().format(timeDoc));
            } else {
                row.put("date", null);
                row.put("time", null);
            }
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
