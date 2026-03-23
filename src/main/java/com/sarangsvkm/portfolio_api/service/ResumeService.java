package com.sarangsvkm.portfolio_api.service;

import com.sarangsvkm.portfolio_api.dto.ResumeDTO;
import com.sarangsvkm.portfolio_api.entity.Profile;
import com.sarangsvkm.portfolio_api.entity.SocialMedia;
import com.sarangsvkm.portfolio_api.repository.ProfileRepository;
import com.sarangsvkm.portfolio_api.repository.ExperienceRepository;
import com.sarangsvkm.portfolio_api.repository.EducationRepository;
import com.sarangsvkm.portfolio_api.repository.SkillRepository;
import com.sarangsvkm.portfolio_api.repository.ProjectRepository;
import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;
import com.sarangsvkm.portfolio_api.entity.Experience;
import com.sarangsvkm.portfolio_api.entity.Education;
import com.sarangsvkm.portfolio_api.entity.Skill;
import com.sarangsvkm.portfolio_api.entity.Project;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("null")
public class ResumeService {

    private final ProfileRepository profileRepo;
    private final ExperienceRepository experienceRepo;
    private final EducationRepository educationRepo;
    private final SkillRepository skillRepo;
    private final ProjectRepository projectRepo;
    private final EncryptionUtils encryptionUtils;

    public ResumeService(
            ProfileRepository profileRepo,
            ExperienceRepository experienceRepo,
            EducationRepository educationRepo,
            SkillRepository skillRepo,
            ProjectRepository projectRepo,
            EncryptionUtils encryptionUtils) {
        this.profileRepo = profileRepo;
        this.experienceRepo = experienceRepo;
        this.educationRepo = educationRepo;
        this.skillRepo = skillRepo;
        this.projectRepo = projectRepo;
        this.encryptionUtils = encryptionUtils;
    }

    // ✅ POST — Save full resume (encrypt + persist all sections)
    public ResumeDTO saveResume(ResumeDTO dto) {
        if (dto == null) throw new IllegalArgumentException("ResumeDTO cannot be null");

        // --- Profile ---
        if (dto.getProfile() != null) {
            Profile p = dto.getProfile();
            p.setName(enc(p.getName()));
            p.setTitle(enc(p.getTitle()));
            p.setAbout(enc(p.getAbout()));
            p.setEmail(enc(p.getEmail()));
            p.setPhone(enc(p.getPhone()));
            p.setLocation(enc(p.getLocation()));
            
            if (p.getSocialMediaLinks() != null) {
                for (SocialMedia sm : p.getSocialMediaLinks()) {
                    sm.setUrl(enc(sm.getUrl()));
                    sm.setProfile(p);
                }
            }
            profileRepo.save(p);
        }

        // --- Experiences ---
        if (dto.getExperiences() != null) {
            for (Experience e : dto.getExperiences()) {
                e.setCompany(enc(e.getCompany()));
                e.setRole(enc(e.getRole()));
                e.setStartDate(enc(e.getStartDate()));
                e.setEndDate(enc(e.getEndDate()));
                e.setDescription(enc(e.getDescription()));
            }
            experienceRepo.saveAll(dto.getExperiences());
        }

        // --- Education ---
        if (dto.getEducations() != null) {
            for (Education e : dto.getEducations()) {
                e.setDegree(enc(e.getDegree()));
                e.setInstitution(enc(e.getInstitution()));
                e.setFieldOfStudy(enc(e.getFieldOfStudy()));
                e.setStartDate(enc(e.getStartDate()));
                e.setEndDate(enc(e.getEndDate()));
            }
            educationRepo.saveAll(dto.getEducations());
        }

        // --- Skills ---
        if (dto.getSkills() != null) {
            for (Skill s : dto.getSkills()) {
                s.setName(enc(s.getName()));
                s.setLevel(enc(s.getLevel()));
                s.setCategory(enc(s.getCategory()));
            }
            skillRepo.saveAll(dto.getSkills());
        }

        // --- Projects ---
        if (dto.getProjects() != null) {
            for (Project p : dto.getProjects()) {
                p.setTitle(enc(p.getTitle()));
                p.setLink(enc(p.getLink()));
                p.setDescription(enc(p.getDescription()));
                p.setTechStack(enc(p.getTechStack()));
            }
            projectRepo.saveAll(dto.getProjects());
        }

        return getResume(); // ✅ Return decrypted data for user feedback
    }


    // ✅ GET — Build full resume (decrypt all sections)
    public ResumeDTO getResume() {
        // --- Profile ---
        List<Profile> profiles = profileRepo.findAll();
        profiles.forEach(p -> {
            p.setName(dec(p.getName()));
            p.setTitle(dec(p.getTitle()));
            p.setAbout(dec(p.getAbout()));
            p.setEmail(dec(p.getEmail()));
            p.setPhone(dec(p.getPhone()));
            p.setLocation(dec(p.getLocation()));
            
            List<SocialMedia> links = p.getSocialMediaLinks();
            if (links != null) {
                for (SocialMedia sm : links) {
                    sm.setUrl(dec(sm.getUrl()));
                }
            }
        });
        Profile profile = profiles.isEmpty() ? null : profiles.get(0);

        // --- Experiences ---
        List<Experience> experiences = experienceRepo.findAll();
        experiences.forEach(e -> {
            e.setCompany(dec(e.getCompany()));
            e.setRole(dec(e.getRole()));
            e.setStartDate(dec(e.getStartDate()));
            e.setEndDate(dec(e.getEndDate()));
            e.setDescription(dec(e.getDescription()));
        });

        // --- Education ---
        List<Education> educations = educationRepo.findAll();
        educations.forEach(e -> {
            e.setDegree(dec(e.getDegree()));
            e.setInstitution(dec(e.getInstitution()));
            e.setFieldOfStudy(dec(e.getFieldOfStudy()));
            e.setStartDate(dec(e.getStartDate()));
            e.setEndDate(dec(e.getEndDate()));
        });

        // --- Skills ---
        List<Skill> skills = skillRepo.findAll();
        skills.forEach(s -> {
            s.setName(dec(s.getName()));
            s.setLevel(dec(s.getLevel()));
            s.setCategory(dec(s.getCategory()));
        });

        // --- Projects ---
        List<Project> projects = projectRepo.findAll();
        projects.forEach(p -> {
            p.setTitle(dec(p.getTitle()));
            p.setLink(dec(p.getLink()));
            p.setDescription(dec(p.getDescription()));
            p.setTechStack(dec(p.getTechStack()));
        });

        return new ResumeDTO(profile, experiences, educations, skills, projects);
    }

    private String enc(String data) {
        try {
            return data == null ? null : encryptionUtils.encrypt(data);
        } catch (Exception e) {
            return data; // Fallback to raw data if encryption fails (or return null)
        }
    }

    private String dec(String data) {
        try {
            return data == null ? null : encryptionUtils.decrypt(data);
        } catch (Exception e) {
            return data; // Fallback to raw data if decryption fails
        }
    }

    public void deleteExperience(Long id) {
        if (id != null) experienceRepo.deleteById(id);
    }

    public void deleteEducation(Long id) {
        if (id != null) educationRepo.deleteById(id);
    }

    public void deleteSkill(Long id) {
        if (id != null) skillRepo.deleteById(id);
    }

    public void deleteProject(Long id) {
        if (id != null) projectRepo.deleteById(id);
    }
}
