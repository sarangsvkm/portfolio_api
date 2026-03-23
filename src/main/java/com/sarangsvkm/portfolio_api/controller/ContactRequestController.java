package com.sarangsvkm.portfolio_api.controller;

import com.sarangsvkm.portfolio_api.entity.ContactRequest;
import com.sarangsvkm.portfolio_api.service.ContactRequestService;
import com.sarangsvkm.portfolio_api.apiuser.ApiUserService;
import com.sarangsvkm.portfolio_api.dto.DeleteRequest;
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
    private final ApiUserService apiUserService;

    public ContactRequestController(ContactRequestService service, ApiUserService apiUserService) {
        this.service = service;
        this.apiUserService = apiUserService;
    }

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

    // ── Report API ─────────────────────────────────────────────────────────────
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
            // otp intentionally excluded for security
            return row;
        }).collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
            @RequestBody DeleteRequest request) {

        try {
            apiUserService.login(request.getUsername(), request.getPassword());
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
