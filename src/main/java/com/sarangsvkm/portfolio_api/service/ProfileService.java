package com.sarangsvkm.portfolio_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;
import com.sarangsvkm.portfolio_api.entity.Profile;
import com.sarangsvkm.portfolio_api.repository.ProfileRepository;

@Service
public class ProfileService {

    private final ProfileRepository repo;
    private final EncryptionUtils encryptionUtils;

    public ProfileService(ProfileRepository repo, EncryptionUtils encryptionUtils) {
        this.repo = repo;
        this.encryptionUtils = encryptionUtils;
    }

    // 🔐 SAVE (Encrypt)
    public Profile save(Profile p) throws Exception {

        p.setName(enc(p.getName()));
        p.setTitle(enc(p.getTitle()));
        p.setAbout(enc(p.getAbout()));
        p.setEmail(enc(p.getEmail()));
        p.setPhone(enc(p.getPhone()));
        p.setLocation(enc(p.getLocation()));

        return repo.save(p); // ✅ return entity
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
        }

        return list;
    }

    // 🔧 Utility methods
    private String enc(String data) throws Exception {
        return data == null ? null : encryptionUtils.encrypt(data);
    }

    private String dec(String data) throws Exception {
        return data == null ? null : encryptionUtils.decrypt(data);
    }

    public Profile update(int id, Profile newData) {

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

        } catch (Exception e) {
            e.printStackTrace(); // 🔍 debug
            throw new RuntimeException("Error updating profile");
        }

        return repo.save(existing);
    }
}