package com.sarangsvkm.portfolio_api.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sarangsvkm.portfolio_api.entity.Experience;
import com.sarangsvkm.portfolio_api.service.ExperienceService;

@RestController
@RequestMapping("/api/experience")
public class ExperienceController {

    private final ExperienceService service;

    public ExperienceController(ExperienceService service) {
        this.service = service;
    }

    // ✅ SAVE EXPERIENCE (Auth handled by Filter)
    @PostMapping
    public ResponseEntity<Experience> save(@RequestBody Experience experience) {
        Experience saved = service.save(experience);
        return ResponseEntity.ok(saved);
    }

    // ✅ UPDATE EXPERIENCE (Auth handled by Filter)
    @PutMapping("/{id}")
    public ResponseEntity<Experience> update(@PathVariable Long id, @RequestBody Experience experience) {
        Experience updated = service.update(id, experience);
        return ResponseEntity.ok(updated);
    }

    // ✅ GET ALL EXPERIENCE (Public)
    @GetMapping
    public ResponseEntity<List<Experience>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ✅ DELETE EXPERIENCE (Auth handled by Filter)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}