package com.sarangsvkm.portfolio_api.service;

import com.sarangsvkm.portfolio_api.dto.ResumeDTO;
import com.sarangsvkm.portfolio_api.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ResumeService {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ExperienceService experienceService;

    @Autowired
    private EducationService educationService;

    @Autowired
    private SkillService skillService;

    @Autowired
    private ProjectService projectService;

    public ResumeDTO getResume(boolean redact) {
        try {
            List<Profile> profiles = redact ? profileService.getAll() : profileService.getRealProfiles();
            Profile profile = profiles.isEmpty() ? null : profiles.get(0);

            return new ResumeDTO(
                profile,
                experienceService.getAll(),
                educationService.getAll(),
                skillService.getAll(),
                projectService.getAll()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch resume data: " + e.getMessage());
        }
    }

    @Transactional
    public ResumeDTO saveResume(ResumeDTO dto, boolean redact) {
        if (dto.getProfile() != null) {
            profileService.save(dto.getProfile());
        }
        if (dto.getExperiences() != null) {
            dto.getExperiences().forEach(experienceService::save);
        }
        if (dto.getEducations() != null) {
            dto.getEducations().forEach(educationService::save);
        }
        if (dto.getSkills() != null) {
            dto.getSkills().forEach(skillService::save);
        }
        if (dto.getProjects() != null) {
            dto.getProjects().forEach(projectService::save);
        }
        return getResume(redact);
    }

    public void deleteExperience(Long id) {
        experienceService.delete(id);
    }

    public void deleteEducation(Long id) {
        educationService.delete(id);
    }

    public void deleteSkill(Long id) {
        skillService.delete(id);
    }

    public void deleteProject(Long id) {
        projectService.delete(id);
    }
}
