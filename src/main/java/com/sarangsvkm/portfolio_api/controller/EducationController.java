package com.sarangsvkm.portfolio_api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.sarangsvkm.portfolio_api.service.EducationService;
import com.sarangsvkm.portfolio_api.entity.Education;
import java.util.List;
import com.sarangsvkm.portfolio_api.apiuser.ApiUserService;
import com.sarangsvkm.portfolio_api.dto.EducationRequest;

@RestController
@RequestMapping("/api/education")
public class EducationController {

    private final EducationService service;
    private final ApiUserService apiUserService;

    public EducationController(EducationService service, ApiUserService apiUserService) {
        this.service = service;
        this.apiUserService = apiUserService;
    }

    // ✅ SAVE EDUCATION (with login check)
    @PostMapping
    public ResponseEntity<?> save(@RequestBody EducationRequest request) {

        try {
            // 🔐 Authenticate user
            apiUserService.login(
                    request.getUsername(),
                    request.getPassword()
            );

            Education savedEducation = service.save(request.getEducation());

            return ResponseEntity.ok(savedEducation);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving education");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id,
            @RequestBody EducationRequest request) {

        try {
            // 🔐 Authenticate
            apiUserService.login(
                    request.getUsername(),
                    request.getPassword()
            );

            Education updated = service.update(id, request.getEducation());

            return ResponseEntity.ok(updated);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating education");
        }
    }
    // ✅ GET ALL EDUCATION
    @GetMapping
    public ResponseEntity<List<Education>> getAll() {

        try {
            List<Education> list = service.getAll();
            return ResponseEntity.ok(list);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}