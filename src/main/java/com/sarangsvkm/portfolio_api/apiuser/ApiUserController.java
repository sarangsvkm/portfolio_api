package com.sarangsvkm.portfolio_api.apiuser;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class ApiUserController {

    private final ApiUserService service;

    public ApiUserController(ApiUserService service) {
        this.service = service;
    }

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ApiUser apiUser) {
        try {
            ApiUser saved = service.register(apiUser);
            saved.setPassword(null); // 🔥 hide password
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().equals("Username already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user");
        }
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ApiUser request) {

        try {
            ApiUser user = service.login(
                    request.getUsername(),
                    request.getPassword()
            );

            user.setPassword(null); // 🔥 hide password

            return ResponseEntity.ok(user);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }
    }
}