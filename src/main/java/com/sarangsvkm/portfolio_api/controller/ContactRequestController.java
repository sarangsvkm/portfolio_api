package com.sarangsvkm.portfolio_api.controller;

import com.sarangsvkm.portfolio_api.entity.ContactRequest;
import com.sarangsvkm.portfolio_api.service.ContactRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin
public class ContactRequestController {

    private final ContactRequestService service;

    public ContactRequestController(ContactRequestService service) {
        this.service = service;
    }

    @PostMapping("/request-otp")
    public Map<String, String> requestOtp(@RequestBody ContactRequest request) {
        if(request.getEmail() == null || request.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        String otp = service.generateAndSaveOtp(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "OTP generated successfully");
        response.put("otp", otp); // Returned in response for testing
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
}
