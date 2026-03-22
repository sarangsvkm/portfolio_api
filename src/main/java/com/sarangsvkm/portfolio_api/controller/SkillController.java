package com.sarangsvkm.portfolio_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.sarangsvkm.portfolio_api.entity.Skill;
import com.sarangsvkm.portfolio_api.service.SkillService;
import com.sarangsvkm.portfolio_api.apiuser.ApiUserService;
import com.sarangsvkm.portfolio_api.dto.SkillRequest;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService service;
    private final ApiUserService apiUserService;

    public SkillController(SkillService service, ApiUserService apiUserService) {
        this.service = service;
        this.apiUserService = apiUserService;
    }

    // ✅ ADD SKILL (with login check)
    @PostMapping
    public ResponseEntity<?> addSkill(@RequestBody SkillRequest request) {

        try {
            // 🔐 Authenticate user
            apiUserService.login(
                    request.getUsername(),
                    request.getPassword()
            );

            Skill savedSkill = service.save(request.getSkill());

            return ResponseEntity.ok(savedSkill);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving skill");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id,
            @RequestBody SkillRequest request) {

        try {
            apiUserService.login(
                    request.getUsername(),
                    request.getPassword()
            );

            Skill updated = service.update(id, request.getSkill());

            return ResponseEntity.ok(updated);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating skill");
        }
    }
    // ✅ GET ALL SKILLS
    @GetMapping
    public ResponseEntity<List<Skill>> getSkills() {

        try {
            List<Skill> list = service.getAll();
            return ResponseEntity.ok(list);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}