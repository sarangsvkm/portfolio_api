package com.sarangsvkm.portfolio_api.controller;

import com.sarangsvkm.portfolio_api.dto.ResumeDTO;
import com.sarangsvkm.portfolio_api.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    // ✅ POST /api/resume — save/update full resume (Auth handled by Filter)
    @PostMapping
    public ResponseEntity<ResumeDTO> saveResume(@RequestBody ResumeDTO dto) {
        ResumeDTO saved = resumeService.saveResume(dto);
        return ResponseEntity.ok(saved);
    }

    // ✅ GET /api/resume — returns full resume data (Public)
    @GetMapping
    public ResponseEntity<ResumeDTO> getResume() {
        ResumeDTO resume = resumeService.getResume();
        return ResponseEntity.ok(resume);
    }

    // ✅ DELETE endpoints (Auth handled by Filter)
    @DeleteMapping("/experience/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        resumeService.deleteExperience(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/education/{id}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long id) {
        resumeService.deleteEducation(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/skill/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        resumeService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/project/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        resumeService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
