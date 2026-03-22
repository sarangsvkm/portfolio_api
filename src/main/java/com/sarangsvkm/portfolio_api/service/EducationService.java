package com.sarangsvkm.portfolio_api.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;
import com.sarangsvkm.portfolio_api.entity.Education;
import com.sarangsvkm.portfolio_api.repository.EducationRepository;


@Service
public class EducationService {

    private final EducationRepository repo;
    private final EncryptionUtils encryptionUtils;

    public EducationService(EducationRepository repo, EncryptionUtils encryptionUtils) {
        this.repo = repo;
        this.encryptionUtils = encryptionUtils;
    }

    // 🔐 SAVE
    public Education save(Education e) {

        e.setDegree(enc(e.getDegree()));
        e.setInstitution(enc(e.getInstitution()));
        e.setYear(enc(e.getYear()));

        return repo.save(e);
    }

    // 🔓 GET
    public List<Education> getAll() {

        List<Education> list = repo.findAll();

        for (Education e : list) {
            e.setDegree(dec(e.getDegree()));
            e.setInstitution(dec(e.getInstitution()));
            e.setYear(dec(e.getYear()));
        }

        return list;
    }

    private String enc(String data) {
        try {
            return data == null ? null : encryptionUtils.encrypt(data);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to encrypt data", ex);
        }
    }

    private String dec(String data) {
        try {
            return data == null ? null : encryptionUtils.decrypt(data);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to decrypt data", ex);
        }
    }
}