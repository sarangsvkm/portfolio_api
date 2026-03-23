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
import com.sarangsvkm.portfolio_api.entity.Image;
import com.sarangsvkm.portfolio_api.service.ProfileService;
import com.sarangsvkm.portfolio_api.service.ImageService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService service;
    private final ApiUserService apiUserService;
    private final ImageService imageService;

    public ProfileController(ProfileService service, ApiUserService apiUserService, ImageService imageService) {
        this.service = service;
        this.apiUserService = apiUserService;
        this.imageService = imageService;
    }

    // ✅ CREATE PROFILE
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

    // ✅ UPLOAD IMAGE TO DB (Separate Table)
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
            
            Image img = imageService.findByProfileId(id);
            if (img == null) img = new Image();
            
            img.setData(file.getBytes());
            img.setType(file.getContentType());
            img.setName(file.getOriginalFilename());
            img.setProfile(profile);
            
            imageService.save(img);
            return ResponseEntity.ok("Image updated successfully in separate table");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file: " + e.getMessage());
        }
    }

    // ✅ VIEW IMAGE FROM DB (Separate Table)
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Image img = imageService.findByProfileId(id);
        if (img == null || img.getData() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(img.getType()))
                .body(img.getData());
    }

    // ✅ DELETE IMAGE FROM DB (Separate Table)
    @DeleteMapping("/image/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable Long id, @RequestBody DeleteRequest request) {
        try {
            apiUserService.login(request.getUsername(), request.getPassword());
            imageService.deleteByProfileId(id);
            return ResponseEntity.ok("Image deleted successfully from separate table");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting image");
        }
    }
}