package com.sarangsvkm.portfolio_api.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.sarangsvkm.portfolio_api.entity.Profile;
import com.sarangsvkm.portfolio_api.repository.ProfileRepository;
import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;

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
}