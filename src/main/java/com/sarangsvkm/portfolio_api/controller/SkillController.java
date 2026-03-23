package com.sarangsvkm.portfolio_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.sarangsvkm.portfolio_api.entity.Skill;
import com.sarangsvkm.portfolio_api.service.SkillService;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService service;

    public SkillController(SkillService service) {
        this.service = service;
    }

    // ✅ ADD SKILL (Auth handled by Filter)
    @PostMapping
    public ResponseEntity<Skill> addSkill(@RequestBody Skill skill) {
        Skill saved = service.save(skill);
        return ResponseEntity.ok(saved);
    }

    // ✅ UPDATE SKILL (Auth handled by Filter)
    @PutMapping("/{id}")
    public ResponseEntity<Skill> update(@PathVariable Long id, @RequestBody Skill skill) {
        Skill updated = service.update(id, skill);
        return ResponseEntity.ok(updated);
    }

    // ✅ GET ALL SKILLS (Public)
    @GetMapping
    public ResponseEntity<List<Skill>> getSkills() {
        return ResponseEntity.ok(service.getAll());
    }

    // ✅ DELETE SKILL (Auth handled by Filter)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}