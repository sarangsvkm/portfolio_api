package com.sarangsvkm.portfolio_api.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;
import com.sarangsvkm.portfolio_api.entity.Profile;
import com.sarangsvkm.portfolio_api.entity.SocialMedia;
import com.sarangsvkm.portfolio_api.repository.ProfileRepository;
import com.sarangsvkm.portfolio_api.repository.SocialMediaRepository;

@Service
@SuppressWarnings("null")
public class ProfileService {

    private final ProfileRepository repo;
    private final SocialMediaRepository socialMediaRepo;
    private final EncryptionUtils encryptionUtils;

    public ProfileService(ProfileRepository repo, SocialMediaRepository socialMediaRepo,
            EncryptionUtils encryptionUtils) {
        this.repo = repo;
        this.socialMediaRepo = socialMediaRepo;
        this.encryptionUtils = encryptionUtils;
    }

    // 🔐 SAVE (Encrypt)
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

        return repo.save(p);
    }

    // 🔓 GET ALL (Decrypt)
    public List<Profile> getAll() throws Exception {
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

        return repo.save(existing);
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