package com.sarangsvkm.portfolio_api.service;

import com.sarangsvkm.portfolio_api.dto.ResumeDTO;
import com.sarangsvkm.portfolio_api.entity.*;
import com.sarangsvkm.portfolio_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResumeService {

    @Autowired
    private ProfileRepository profileRepo;

    @Autowired
    private ExperienceRepository experienceRepo;

    @Autowired
    private EducationRepository educationRepo;

    @Autowired
    private SkillRepository skillRepo;

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private ImageService imageService;

    private String dec(String data) {
        if (data == null) return null;
        return encryptionService.decrypt(data);
    }

    public ResumeDTO getResume() {
        // --- Profile (Detached & Redacted) ---
        List<Profile> profiles = profileRepo.findAll();
        Profile profile = null;
        if (!profiles.isEmpty()) {
            profile = copyAndRedactProfile(profiles.get(0));
        }

        // --- Experiences ---
        List<Experience> experiences = experienceRepo.findAll();
        experiences.forEach(e -> {
            e.setRole(dec(e.getRole()));
            e.setCompany(dec(e.getCompany()));
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
            e.setDescription(dec(e.getDescription()));
        });

        // --- Skills ---
        List<Skill> skills = skillRepo.findAll();
        skills.forEach(s -> {
            s.setName(dec(s.getName()));
            s.setCategory(dec(s.getCategory()));
        });

        // --- Projects ---
        List<Project> projects = projectRepo.findAll();
        projects.forEach(p -> {
            p.setTitle(dec(p.getTitle()));
            p.setDescription(dec(p.getDescription()));
            p.setTechStack(dec(p.getTechStack()));
            p.setLink(dec(p.getLink()));
            p.setGithubLink(dec(p.getGithubLink()));
        });

        ResumeDTO resume = new ResumeDTO();
        resume.setProfile(profile);
        resume.setExperiences(experiences);
        resume.setEducations(educations);
        resume.setSkills(skills);
        resume.setProjects(projects);

        return resume;
    }

    /**
     * Creates a DETACHED copy of the profile and redacts sensitive info.
     * This prevents Hibernate from auto-saving masked strings to the DB.
     */
    private Profile copyAndRedactProfile(Profile source) {
        Profile p = new Profile();
        p.setId(source.getId());
        p.setName(dec(source.getName()));
        p.setTitle(dec(source.getTitle()));
        p.setAbout(dec(source.getAbout()));
        p.setEmail(dec(source.getEmail()));
        p.setLocation(dec(source.getLocation()));
        p.setImageUrl(dec(source.getImageUrl()));
        p.setBannerUrl(dec(source.getBannerUrl()));
        
        // Always redact for the public resume view
        p.setPhone("+91 ••••• ••07"); 
        p.setResumeUrl("");

        // Copy social media links (detached)
        if (source.getSocialMediaLinks() != null) {
            List<SocialMedia> links = new ArrayList<>();
            for (SocialMedia sm : source.getSocialMediaLinks()) {
                SocialMedia copy = new SocialMedia();
                copy.setId(sm.getId());
                copy.setPlatform(sm.getPlatform());
                copy.setUrl(dec(sm.getUrl()));
                links.add(copy);
            }
            p.setSocialMediaLinks(links);
        }

        // Set Image URL if exists
        Image image = imageService.findByProfileId(p.getId());
        if (image != null) {
            p.setImageUrl("/portfolioApi/api/profile/image/" + p.getId());
        }

        return p;
    }
}
