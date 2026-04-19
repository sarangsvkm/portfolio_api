package com.sarangsvkm.portfolio_api.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;
import com.sarangsvkm.portfolio_api.entity.Image;
import com.sarangsvkm.portfolio_api.entity.Profile;
import com.sarangsvkm.portfolio_api.entity.SocialMedia;
import com.sarangsvkm.portfolio_api.repository.ProfileRepository;
import com.sarangsvkm.portfolio_api.repository.SocialMediaRepository;

@Service
@SuppressWarnings("null")
public class ProfileService {

    private final ProfileRepository repo;
    private final SocialMediaRepository socialMediaRepo;
    private final ImageService imageService;
    private final EncryptionUtils encryptionUtils;

    public ProfileService(ProfileRepository repo, SocialMediaRepository socialMediaRepo,
            ImageService imageService, EncryptionUtils encryptionUtils) {
        this.repo = repo;
        this.socialMediaRepo = socialMediaRepo;
        this.imageService = imageService;
        this.encryptionUtils = encryptionUtils;
    }

    // 🔐 SAVE (Encrypt before storing, decrypt before returning)
    public Profile save(Profile p) throws Exception {
        if (p == null)
            throw new IllegalArgumentException("Profile cannot be null");

        p.setName(enc(p.getName()));
        p.setTitle(enc(p.getTitle()));
        p.setAbout(enc(p.getAbout()));
        p.setEmail(enc(p.getEmail()));
        p.setPhone(enc(p.getPhone()));
        p.setLocation(enc(p.getLocation()));
        p.setImageUrl(enc(p.getImageUrl()));
        p.setBannerUrl(enc(p.getBannerUrl()));
        p.setResumeUrl(enc(p.getResumeUrl()));

        if (p.getSocialMediaLinks() != null) {
            for (SocialMedia sm : p.getSocialMediaLinks()) {
                sm.setUrl(enc(sm.getUrl()));
                sm.setProfile(p);
            }
        }

        Profile saved = repo.save(p);

        // Decrypt before returning so the frontend always gets plain text
        saved.setName(dec(saved.getName()));
        saved.setTitle(dec(saved.getTitle()));
        saved.setAbout(dec(saved.getAbout()));
        saved.setEmail(dec(saved.getEmail()));
        saved.setPhone(dec(saved.getPhone()));
        saved.setLocation(dec(saved.getLocation()));
        saved.setImageUrl(dec(saved.getImageUrl()));
        saved.setBannerUrl(dec(saved.getBannerUrl()));
        saved.setResumeUrl(dec(saved.getResumeUrl()));

        if (saved.getSocialMediaLinks() != null) {
            for (SocialMedia sm : saved.getSocialMediaLinks()) {
                sm.setUrl(dec(sm.getUrl()));
            }
        }

        return saved;
    }

    // 🔓 GET ALL (Redacted for Public)
    public List<Profile> getAll() throws Exception {
        List<Profile> list = repo.findAll();
        for (Profile p : list) {
            p.setName(dec(p.getName()));
            p.setTitle(dec(p.getTitle()));
            p.setAbout(dec(p.getAbout()));
            p.setEmail(dec(p.getEmail()));
            p.setPhone("+91 ••••• ••07"); // Masked
            p.setLocation(dec(p.getLocation()));
            p.setImageUrl(dec(p.getImageUrl()));
            p.setBannerUrl(dec(p.getBannerUrl()));
            p.setResumeUrl(""); // Redacted
            
            // ... (rest of decryption)
            List<SocialMedia> links = p.getSocialMediaLinks();
            if (links != null) {
                for (SocialMedia sm : links) {
                    sm.setUrl(dec(sm.getUrl()));
                }
            }
            Image image = imageService.findByProfileId(p.getId());
            if (image != null) {
                p.setImageUrl("/portfolioApi/api/profile/image/" + p.getId());
            }
            p.setProfileImage(null);
        }
        return list;
    }

    // 🔐 GET REAL (For internal use/verification)
    public List<Profile> getRealProfiles() throws Exception {
        List<Profile> list = repo.findAll();
        for (Profile p : list) {
            p.setName(dec(p.getName()));
            p.setTitle(dec(p.getTitle()));
            p.setAbout(dec(p.getAbout()));
            p.setEmail(dec(p.getEmail()));
            p.setPhone(dec(p.getPhone()));
            p.setLocation(dec(p.getLocation()));
            p.setImageUrl(dec(p.getImageUrl()));
            p.setBannerUrl(dec(p.getBannerUrl()));
            p.setResumeUrl(dec(p.getResumeUrl()));
            
            List<SocialMedia> links = p.getSocialMediaLinks();
            if (links != null) {
                for (SocialMedia sm : links) {
                    sm.setUrl(dec(sm.getUrl()));
                }
            }
        }
        return list;
    }

    public Profile update(Long id, Profile newData) {
        if (id == null)
            throw new IllegalArgumentException("ID cannot be null");

        Profile existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        try {
            if (newData.getName() != null)
                existing.setName(enc(newData.getName()));
            if (newData.getTitle() != null)
                existing.setTitle(enc(newData.getTitle()));
            if (newData.getEmail() != null)
                existing.setEmail(enc(newData.getEmail()));
            if (newData.getPhone() != null)
                existing.setPhone(enc(newData.getPhone()));
            if (newData.getLocation() != null)
                existing.setLocation(enc(newData.getLocation()));
            if (newData.getAbout() != null)
                existing.setAbout(enc(newData.getAbout()));
            if (newData.getImageUrl() != null)
                existing.setImageUrl(enc(newData.getImageUrl()));
            if (newData.getBannerUrl() != null)
                existing.setBannerUrl(enc(newData.getBannerUrl()));
            if (newData.getResumeUrl() != null)
                existing.setResumeUrl(enc(newData.getResumeUrl()));

            if (newData.getSocialMediaLinks() != null) {
                for (SocialMedia sm : newData.getSocialMediaLinks()) {
                    sm.setUrl(enc(sm.getUrl()));
                    sm.setProfile(existing);
                }
                existing.getSocialMediaLinks().clear();
                existing.getSocialMediaLinks().addAll(newData.getSocialMediaLinks());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error updating profile");
        }

        Profile saved = repo.save(existing);

        // Decrypt before returning so the frontend always gets plain text
        try {
            saved.setName(dec(saved.getName()));
            saved.setTitle(dec(saved.getTitle()));
            saved.setAbout(dec(saved.getAbout()));
            saved.setEmail(dec(saved.getEmail()));
            saved.setPhone(dec(saved.getPhone()));
            saved.setLocation(dec(saved.getLocation()));
            saved.setImageUrl(dec(saved.getImageUrl()));
            saved.setBannerUrl(dec(saved.getBannerUrl()));
            saved.setResumeUrl(dec(saved.getResumeUrl()));

            if (saved.getSocialMediaLinks() != null) {
                for (SocialMedia sm : saved.getSocialMediaLinks()) {
                    sm.setUrl(dec(sm.getUrl()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting updated profile");
        }

        return saved;
    }

    public void delete(Long id) {
        if (id != null) {
            repo.deleteById(id);
        }
    }

    public Profile findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void deleteSocialMedia(Long id) {
        if (id != null) {
            socialMediaRepo.deleteById(id);
        }
    }

    private String enc(String data) throws Exception {
        return data == null ? null : encryptionUtils.encrypt(data);
    }

    private String dec(String data) throws Exception {
        return data == null ? null : encryptionUtils.decrypt(data);
    }
}
