package com.sarangsvkm.portfolio_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.sarangsvkm.portfolio_api.entity.Project;
import com.sarangsvkm.portfolio_api.service.ProjectService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    // ✅ SAVE PROJECT (Auth handled by Filter)
    @PostMapping
    public ResponseEntity<Project> save(@RequestBody Project project) {
        Project saved = service.save(project);
        return ResponseEntity.ok(saved);
    }

    // ✅ UPDATE PROJECT (Auth handled by Filter)
    @PutMapping("/{id}")
    public ResponseEntity<Project> update(@PathVariable Long id, @RequestBody Project project) {
        Project updated = service.update(id, project);
        return ResponseEntity.ok(updated);
    }

    // ✅ GET ALL PROJECTS (Public)
    @GetMapping
    public ResponseEntity<List<Project>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ✅ DELETE PROJECT (Auth handled by Filter)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}