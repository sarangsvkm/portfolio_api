package com.sarangsvkm.portfolio_api.service;


import org.springframework.stereotype.Service;
import java.util.List;

import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;
import com.sarangsvkm.portfolio_api.entity.Experience;
import com.sarangsvkm.portfolio_api.repository.ExperienceRepository;

@Service
public class ExperienceService {

    private final ExperienceRepository repo;
    private final EncryptionUtils encryptionUtils;

    public ExperienceService(ExperienceRepository repo, EncryptionUtils encryptionUtils) {
        this.repo = repo;
        this.encryptionUtils = encryptionUtils;
    }

    // 🔐 SAVE
    public Experience save(Experience e) {

        e.setRole(enc(e.getRole()));
        e.setCompany(enc(e.getCompany()));
        e.setDuration(enc(e.getDuration()));
        e.setDescription(enc(e.getDescription()));

        return repo.save(e);
    }

    // 🔓 GET
    public List<Experience> getAll() {

        List<Experience> list = repo.findAll();

        for (Experience e : list) {
            e.setRole(dec(e.getRole()));
            e.setCompany(dec(e.getCompany()));
            e.setDuration(dec(e.getDuration()));
            e.setDescription(dec(e.getDescription()));
        }

        return list;
    }

    private String enc(String data) {
        try {
            return data == null ? null : encryptionUtils.encrypt(data);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    private String dec(String data) {
        try {
            return data == null ? null : encryptionUtils.decrypt(data);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }
}