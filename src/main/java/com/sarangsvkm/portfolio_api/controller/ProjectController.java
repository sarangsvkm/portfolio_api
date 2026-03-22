package com.sarangsvkm.portfolio_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import com.sarangsvkm.portfolio_api.entity.Project;
import com.sarangsvkm.portfolio_api.service.ProjectService;
import com.sarangsvkm.portfolio_api.apiuser.ApiUserService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService service;
    private final ApiUserService apiUserService;

    public ProjectController(ProjectService service, ApiUserService apiUserService) {
        this.service = service;
        this.apiUserService = apiUserService;
    }

    // ✅ SAVE PROJECT (with login check)
    @PostMapping
    public ResponseEntity<?> save(@RequestBody ProjectRequest request) {

        try {
            // 🔐 Authenticate user
            apiUserService.login(
                    request.getUsername(),
                    request.getPassword());

            Project saved = service.save(request.getProject());

            return ResponseEntity.ok(saved);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving project");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id,
            @RequestBody ProjectRequest request) {

        try {
            apiUserService.login(
                    request.getUsername(),
                    request.getPassword());

            Project updated = service.update(id, request.getProject());

            return ResponseEntity.ok(updated);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating project");
        }
    }

    // ✅ GET ALL PROJECTS
    @GetMapping
    public ResponseEntity<List<Project>> getAll() {

        try {
            List<Project> list = service.getAll();
            return ResponseEntity.ok(list);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

}