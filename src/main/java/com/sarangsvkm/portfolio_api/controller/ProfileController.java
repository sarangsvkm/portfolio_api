package com.sarangsvkm.portfolio_api.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sarangsvkm.portfolio_api.apiuser.ApiUserService;
import com.sarangsvkm.portfolio_api.dto.ProfileRequest;
import com.sarangsvkm.portfolio_api.entity.Profile;
import com.sarangsvkm.portfolio_api.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService service;
    private final ApiUserService apiUserService;

    public ProfileController(ProfileService service, ApiUserService apiUserService) {
        this.service = service;
        this.apiUserService = apiUserService;
    }

    // ✅ CREATE PROFILE WITH LOGIN CHECK
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProfileRequest request) {

        try {
            // 🔐 Authenticate user
            apiUserService.login(
                    request.getUsername(),
                    request.getPassword()
            );

            // 💾 Save profile
            Profile savedProfile = service.save(request.getProfile());

            return ResponseEntity.ok(savedProfile);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving profile");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(
            @PathVariable int id,
            @RequestBody ProfileRequest request) {

        try {
            // 🔐 Authenticate user
            apiUserService.login(
                    request.getUsername(),
                    request.getPassword()
            );

            Profile updated = service.update(id, request.getProfile());

            return ResponseEntity.ok(updated);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating profile");
        }
    }
    // ✅ GET ALL PROFILES
    @GetMapping
    public ResponseEntity<List<Profile>> getAll() {

        try {
            List<Profile> profiles = service.getAll();
            return ResponseEntity.ok(profiles);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}