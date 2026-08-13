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
    private final com.sarangsvkm.portfolio_api.service.ResumeJsonService resumeJsonService;
    private final com.sarangsvkm.portfolio_api.service.ResumeParserService resumeParserService;

    public ResumeController(ResumeService resumeService, ApiUserService apiUserService, com.sarangsvkm.portfolio_api.service.ResumeJsonService resumeJsonService, com.sarangsvkm.portfolio_api.service.ResumeParserService resumeParserService) {
        this.resumeService = resumeService;
        this.apiUserService = apiUserService;
        this.resumeJsonService = resumeJsonService;
        this.resumeParserService = resumeParserService;
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
        // Trigger cache update since the resume was modified
        resumeJsonService.updateCache();
        return ResponseEntity.ok(saved);
    }

    // ✅ GET /api/resume — returns full resume data (Public)
    @GetMapping
    public ResponseEntity<ResumeDTO> getResume() {
        return ResponseEntity.ok(resumeJsonService.getCachedResume());
    }

    // ✅ DELETE endpoints (Auth handled by Filter)
    @DeleteMapping("/experience/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        resumeService.deleteExperience(id);
        resumeJsonService.updateCache();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/education/{id}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long id) {
        resumeService.deleteEducation(id);
        resumeJsonService.updateCache();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/skill/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        resumeService.deleteSkill(id);
        resumeJsonService.updateCache();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/project/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        resumeService.deleteProject(id);
        resumeJsonService.updateCache();
        return ResponseEntity.noContent().build();
    }

    // ✅ POST /api/resume/parse — extracts and parses resume PDF using Gemini AI (Auth handled by Filter)
    @PostMapping("/parse")
    public ResponseEntity<ResumeDTO> parseResume(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) throws Exception {
        String text = resumeParserService.extractTextFromPdf(file);
        ResumeDTO parsed = resumeParserService.parseResumeText(text);
        return ResponseEntity.ok(parsed);
    }
}
