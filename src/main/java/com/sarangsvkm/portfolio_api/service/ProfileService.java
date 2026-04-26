package com.sarangsvkm.portfolio_api.service;

import com.sarangsvkm.portfolio_api.entity.Image;
import com.sarangsvkm.portfolio_api.entity.Profile;
import com.sarangsvkm.portfolio_api.entity.SocialMedia;
import com.sarangsvkm.portfolio_api.repository.ProfileRepository;
import com.sarangsvkm.portfolio_api.repository.SocialMediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository repo;

    @Autowired
    private SocialMediaRepository socialMediaRepo;

    @Autowired
    private EncryptionUtils encryptionUtils;

    @Autowired
    private ImageService imageService;

    private String enc(String data) {
        return encryptionUtils.encrypt(data);
    }

    private String dec(String data) {
        return encryptionUtils.decrypt(data);
    }

    public Profile save(Profile profile) {
        if (profile == null)
            throw new IllegalArgumentException("Profile cannot be null");

        profile.setName(enc(profile.getName()));
        profile.setTitle(enc(profile.getTitle()));
        profile.setAbout(enc(profile.getAbout()));
        profile.setEmail(enc(profile.getEmail()));
        profile.setPhone(enc(profile.getPhone()));
        profile.setLocation(enc(profile.getLocation()));
        profile.setImageUrl(enc(profile.getImageUrl()));
        profile.setBannerUrl(enc(profile.getBannerUrl()));
        profile.setResumeUrl(enc(profile.getResumeUrl()));

        Profile saved = repo.save(profile);

        if (profile.getSocialMediaLinks() != null) {
            for (SocialMedia link : profile.getSocialMediaLinks()) {
                link.setProfile(saved);
                link.setUrl(enc(link.getUrl()));
                socialMediaRepo.save(link);
            }
        }
        return copyAndProcess(saved, false); // ✅ Return decrypted
    }

    // 🔓 GET ALL (Redacted for Public)
    public List<Profile> getAll() throws Exception {
        return repo.findAll().stream()
                .map(p -> copyAndProcess(p, true))
                .collect(Collectors.toList());
    }

    // 🔐 GET REAL (For internal use/verification)
    public List<Profile> getRealProfiles() throws Exception {
        return repo.findAll().stream()
                .map(p -> copyAndProcess(p, false))
                .collect(Collectors.toList());
    }

    /**
     * Creates a NEW Profile object (detached) to prevent Hibernate from 
     * auto-saving redacted strings back to the database.
     */
    private Profile copyAndProcess(Profile source, boolean redact) {
        Profile p = new Profile();
        p.setId(source.getId());
        p.setName(dec(source.getName()));
        p.setTitle(dec(source.getTitle()));
        p.setAbout(dec(source.getAbout()));
        p.setEmail(dec(source.getEmail()));
        p.setLocation(dec(source.getLocation()));
        p.setImageUrl(dec(source.getImageUrl()));
        p.setBannerUrl(dec(source.getBannerUrl()));

        if (redact) {
            p.setPhone(""); // Hidden entirely when redacted
            p.setResumeUrl(""); // Redacted
        } else {
            p.setPhone(dec(source.getPhone()));
            p.setResumeUrl(dec(source.getResumeUrl()));
        }

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

    public Profile update(Long id, Profile newData) {
        if (id == null)
            throw new IllegalArgumentException("ID cannot be null");
        return repo.findById(id).map(existing -> {
            if (newData.getName() != null) existing.setName(enc(newData.getName()));
            if (newData.getTitle() != null) existing.setTitle(enc(newData.getTitle()));
            if (newData.getAbout() != null) existing.setAbout(enc(newData.getAbout()));
            if (newData.getEmail() != null) existing.setEmail(enc(newData.getEmail()));
            if (newData.getPhone() != null) existing.setPhone(enc(newData.getPhone()));
            if (newData.getLocation() != null) existing.setLocation(enc(newData.getLocation()));
            if (newData.getImageUrl() != null) existing.setImageUrl(enc(newData.getImageUrl()));
            if (newData.getBannerUrl() != null) existing.setBannerUrl(enc(newData.getBannerUrl()));
            if (newData.getResumeUrl() != null) existing.setResumeUrl(enc(newData.getResumeUrl()));
            
            Profile saved = repo.save(existing);
            return copyAndProcess(saved, false); // ✅ Return decrypted
        }).orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public Profile findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public void deleteSocialMedia(Long id) {
        socialMediaRepo.deleteById(id);
    }
}
