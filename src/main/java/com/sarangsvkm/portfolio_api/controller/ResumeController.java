package com.sarangsvkm.portfolio_api.controller;

import com.sarangsvkm.portfolio_api.apiuser.ApiUserService;
import com.sarangsvkm.portfolio_api.dto.ResumeDTO;
import com.sarangsvkm.portfolio_api.dto.DeleteRequest;
import com.fasterxml.jackson.annotation.JsonAlias;

import com.sarangsvkm.portfolio_api.entity.Education;
import com.sarangsvkm.portfolio_api.entity.Experience;
import com.sarangsvkm.portfolio_api.entity.Profile;
import com.sarangsvkm.portfolio_api.entity.Project;
import com.sarangsvkm.portfolio_api.entity.Skill;
import com.sarangsvkm.portfolio_api.service.ResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeService resumeService;
    private final ApiUserService apiUserService;

    public ResumeController(ResumeService resumeService, ApiUserService apiUserService) {
        this.resumeService = resumeService;
        this.apiUserService = apiUserService;
    }

    // ✅ POST /api/resume — save full resume (auth required)
    @PostMapping
    public ResponseEntity<?> saveResume(@RequestBody ResumeRequest request) {
        // 🔐 Auth
        try {
            apiUserService.login(request.getUsername(), request.getPassword());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        // 💾 Build DTO from flat request fields and save
        try {
            ResumeDTO dto = new ResumeDTO(
                    request.getProfile(),
                    request.getExperiences(),
                    request.getEducations(),
                    request.getSkills(),
                    request.getProjects()
            );
            ResumeDTO saved = resumeService.saveResume(dto);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving resume: " + e.getMessage());
        }
    }

    // ✅ GET /api/resume — returns full resume data
    @GetMapping
    public ResponseEntity<?> getResume() {
        try {
            ResumeDTO resume = resumeService.getResume();
            return ResponseEntity.ok(resume);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error building resume: " + e.getMessage());
        }
    }

    // ── Flat request wrapper (no nested "resume" key needed) ──────────────────
    public static class ResumeRequest {
        private String username;
        private String password;
        private Profile profile;

        @JsonAlias({"experience", "experiences"})
        private List<Experience> experiences;

        @JsonAlias({"education", "educations"})
        private List<Education> educations;

        private List<Skill> skills;
        private List<Project> projects;


        public String getUsername() { return username; }
        public void setUsername(String u) { this.username = u; }
        public String getPassword() { return password; }
        public void setPassword(String p) { this.password = p; }
        public Profile getProfile() { return profile; }
        public void setProfile(Profile profile) { this.profile = profile; }
        public List<Experience> getExperiences() { return experiences; }
        public void setExperiences(List<Experience> e) { this.experiences = e; }
        public List<Education> getEducations() { return educations; }
        public void setEducations(List<Education> e) { this.educations = e; }
        public List<Skill> getSkills() { return skills; }
        public void setSkills(List<Skill> s) { this.skills = s; }
        public List<Project> getProjects() { return projects; }
        public void setProjects(List<Project> p) { this.projects = p; }
    }

    @DeleteMapping("/experience/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id,
            @RequestBody DeleteRequest request) {
        try {
            apiUserService.login(request.getUsername(), request.getPassword());
            resumeService.deleteExperience(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/education/{id}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long id,
            @RequestBody DeleteRequest request) {
        try {
            apiUserService.login(request.getUsername(), request.getPassword());
            resumeService.deleteEducation(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/skill/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id,
            @RequestBody DeleteRequest request) {
        try {
            apiUserService.login(request.getUsername(), request.getPassword());
            resumeService.deleteSkill(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/project/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id,
            @RequestBody DeleteRequest request) {
        try {
            apiUserService.login(request.getUsername(), request.getPassword());
            resumeService.deleteProject(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
