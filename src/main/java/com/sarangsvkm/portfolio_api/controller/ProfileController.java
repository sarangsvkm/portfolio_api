package com.sarangsvkm.portfolio_api.controller;

import java.util.List;
import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sarangsvkm.portfolio_api.apiuser.ApiUserService;
import com.sarangsvkm.portfolio_api.dto.ProfileRequest;
import com.sarangsvkm.portfolio_api.dto.DeleteRequest;
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
            apiUserService.login(request.getUsername(), request.getPassword());
            Profile savedProfile = service.save(request.getProfile());
            return ResponseEntity.ok(savedProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving profile");
        }
    }

    // ✅ UPDATE PROFILE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody ProfileRequest request) {
        try {
            apiUserService.login(request.getUsername(), request.getPassword());
            Profile updated = service.update(id, request.getProfile());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating profile");
        }
    }

    // ✅ GET ALL PROFILES
    @GetMapping
    public ResponseEntity<List<Profile>> getAll() {
        try {
            List<Profile> profiles = service.getAll();
            return ResponseEntity.ok(profiles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // ✅ DELETE PROFILE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestBody DeleteRequest request) {
        try {
            apiUserService.login(request.getUsername(), request.getPassword());
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting profile");
        }
    }

    // ✅ DELETE SOCIAL MEDIA LINK
    @DeleteMapping("/social/{id}")
    public ResponseEntity<?> deleteSocial(@PathVariable Long id, @RequestBody DeleteRequest request) {
        try {
            apiUserService.login(request.getUsername(), request.getPassword());
            service.deleteSocialMedia(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting social media link");
        }
    }

    // ✅ UPLOAD IMAGE TO DB
    @PostMapping("/image/{id}")
    public ResponseEntity<?> uploadImage(
            @PathVariable Long id,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("file") MultipartFile file) {
        
        try {
            apiUserService.login(username, password);
            Profile profile = service.findById(id);
            if (profile == null) return ResponseEntity.notFound().build();
            
            profile.setProfileImage(file.getBytes());
            profile.setImageType(file.getContentType());
            service.updateRaw(profile);
            return ResponseEntity.ok("Image updated successfully");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file: " + e.getMessage());
        }
    }

    // ✅ VIEW IMAGE FROM DB
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Profile profile = service.findById(id);
        if (profile == null || profile.getProfileImage() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(profile.getImageType()))
                .body(profile.getProfileImage());
    }
}