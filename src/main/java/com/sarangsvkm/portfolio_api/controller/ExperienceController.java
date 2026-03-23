package com.sarangsvkm.portfolio_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sarangsvkm.portfolio_api.entity.Experience;
import com.sarangsvkm.portfolio_api.service.ExperienceService;
import com.sarangsvkm.portfolio_api.apiuser.ApiUserService;
import com.sarangsvkm.portfolio_api.dto.ExperienceRequest;
import com.sarangsvkm.portfolio_api.dto.DeleteRequest;

@RestController
@RequestMapping("/api/experience")
public class ExperienceController {

    private final ExperienceService service;
    private final ApiUserService apiUserService;

    public ExperienceController(ExperienceService service, ApiUserService apiUserService) {
        this.service = service;
        this.apiUserService = apiUserService;
    }

    // ✅ SAVE EXPERIENCE (with login check)
    @PostMapping
    public ResponseEntity<?> save(@RequestBody ExperienceRequest request) {

        try {
            // 🔐 Authenticate user
            apiUserService.login(
                    request.getUsername(),
                    request.getPassword()
            );

            Experience saved = service.save(request.getExperience());

            return ResponseEntity.ok(saved);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving experience");
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
            @RequestBody ExperienceRequest request) {

        try {
            apiUserService.login(
                    request.getUsername(),
                    request.getPassword()
            );

            Experience updated = service.update(id, request.getExperience());

            return ResponseEntity.ok(updated);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating experience");
        }
    }
    // ✅ GET ALL EXPERIENCE
    @GetMapping
    public ResponseEntity<List<Experience>> getAll() {

        try {
            List<Experience> list = service.getAll();
            return ResponseEntity.ok(list);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
            @RequestBody DeleteRequest request) {

        try {
            apiUserService.login(request.getUsername(), request.getPassword());
            service.delete(id);
            return ResponseEntity.noContent().build();

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting experience");
        }
    }
}