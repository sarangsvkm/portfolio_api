package com.sarangsvkm.portfolio_api.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.sarangsvkm.portfolio_api.service.EducationService;
import com.sarangsvkm.portfolio_api.entity.Education;
import java.util.List;

@RestController
@RequestMapping("/api/education")
public class EducationController {

    private final EducationService service;

    public EducationController(EducationService service) {
        this.service = service;
    }

    // ✅ SAVE EDUCATION (Auth handled by Filter)
    @PostMapping
    public ResponseEntity<Education> save(@RequestBody Education education) {
        Education saved = service.save(education);
        return ResponseEntity.ok(saved);
    }

    // ✅ UPDATE EDUCATION (Auth handled by Filter)
    @PutMapping("/{id}")
    public ResponseEntity<Education> update(@PathVariable Long id, @RequestBody Education education) {
        Education updated = service.update(id, education);
        return ResponseEntity.ok(updated);
    }

    // ✅ GET ALL EDUCATION (Public)
    @GetMapping
    public ResponseEntity<List<Education>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ✅ DELETE EDUCATION (Auth handled by Filter)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}