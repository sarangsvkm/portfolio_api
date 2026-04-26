package com.sarangsvkm.portfolio_api.controller;

import com.sarangsvkm.portfolio_api.dto.ResumeDTO;
import com.sarangsvkm.portfolio_api.service.ResumeService;
import com.sarangsvkm.portfolio_api.apiuser.ApiUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeService resumeService;
    private final ApiUserService apiUserService;

    public ResumeController(ResumeService resumeService, ApiUserService apiUserService) {
        this.resumeService = resumeService;
        this.apiUserService = apiUserService;
    }

    // ✅ POST /api/resume — save/update full resume (Auth handled by Filter)
    @PostMapping
    public ResponseEntity<ResumeDTO> saveResume(
            @RequestBody ResumeDTO dto,
            @RequestHeader(value = "X-Admin-Username", required = false) String username,
            @RequestHeader(value = "X-Admin-Password", required = false) String password) {
        
        boolean redact = true;
        if (username != null && password != null) {
            try {
                apiUserService.login(username, password);
                redact = false;
            } catch (Exception e) {
                // Invalid credentials, fall back to redacted
            }
        }
        ResumeDTO saved = resumeService.saveResume(dto, redact);
        return ResponseEntity.ok(saved);
    }

    // ✅ GET /api/resume — returns full resume data (Public)
    @GetMapping
    public ResponseEntity<ResumeDTO> getResume() {
        ResumeDTO resume = resumeService.getResume(true);
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
