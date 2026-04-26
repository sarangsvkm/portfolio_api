package com.sarangsvkm.portfolio_api.controller;

import java.util.List;
import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sarangsvkm.portfolio_api.entity.Profile;
import com.sarangsvkm.portfolio_api.entity.Image;
import com.sarangsvkm.portfolio_api.service.ProfileService;
import com.sarangsvkm.portfolio_api.service.ImageService;
import com.sarangsvkm.portfolio_api.apiuser.ApiUserService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService service;
    private final ImageService imageService;
    private final ApiUserService apiUserService;

    public ProfileController(ProfileService service, ImageService imageService, ApiUserService apiUserService) {
        this.service = service;
        this.imageService = imageService;
        this.apiUserService = apiUserService;
    }

    // ✅ CREATE PROFILE (Auth handled by Filter)
    @PostMapping
    public ResponseEntity<Profile> create(@RequestBody Profile profile) throws Exception {
        Profile savedProfile = service.save(profile);
        return ResponseEntity.ok(savedProfile);
    }

    // ✅ UPDATE PROFILE (Auth handled by Filter)
    @PutMapping("/{id}")
    public ResponseEntity<Profile> updateProfile(@PathVariable Long id, @RequestBody Profile profile) {
        Profile updated = service.update(id, profile);
        return ResponseEntity.ok(updated);
    }

    // ✅ GET ALL PROFILES (Public / Admin)
    @GetMapping
    public ResponseEntity<List<Profile>> getAll(
            @RequestHeader(value = "X-Admin-Username", required = false) String username,
            @RequestHeader(value = "X-Admin-Password", required = false) String password) throws Exception {
        
        if (username != null && password != null) {
            try {
                apiUserService.login(username, password);
                return ResponseEntity.ok(service.getRealProfiles());
            } catch (Exception e) {
                // Invalid credentials, fall back to public view
            }
        }
        return ResponseEntity.ok(service.getAll());
    }

    // ✅ DELETE PROFILE (Auth handled by Filter)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ DELETE SOCIAL MEDIA LINK (Auth handled by Filter)
    @DeleteMapping("/social/{id}")
    public ResponseEntity<Void> deleteSocial(@PathVariable Long id) {
        service.deleteSocialMedia(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ UPLOAD IMAGE TO DB (Auth handled by Filter)
    @PostMapping("/image/{id}")
    public ResponseEntity<String> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {
        
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
    }

    // ✅ VIEW IMAGE FROM DB (Public)
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

    @GetMapping("/image/name/{name}")
    public ResponseEntity<byte[]> getImageByName(@PathVariable String name) {
        Image img = imageService.findByName(name);
        if (img == null || img.getData() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(img.getType()))
                .body(img.getData());
    }

    // ✅ DELETE IMAGE FROM DB (Auth handled by Filter)
    @DeleteMapping("/image/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {
        imageService.deleteByProfileId(id);
        return ResponseEntity.ok("Image deleted successfully from separate table");
    }
}
