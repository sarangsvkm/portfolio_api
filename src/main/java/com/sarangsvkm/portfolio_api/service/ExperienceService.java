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

    public Experience save(Experience e) {
        if (e == null) throw new IllegalArgumentException("Experience cannot be null");
        encrypt(e);
        Experience saved = repo.save(e);
        decrypt(saved);
        return saved;
    }

    public List<Experience> getAll() {
        List<Experience> list = repo.findAll();
        list.forEach(this::decrypt);
        return list;
    }

    public Experience update(Long id, Experience newData) {
        if (id == null) throw new IllegalArgumentException("ID cannot be null");
        Experience existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Experience not found"));

        if (newData.getCompany() != null)
            existing.setCompany(enc(newData.getCompany()));

        if (newData.getRole() != null)
            existing.setRole(enc(newData.getRole()));

        if (newData.getStartDate() != null)
            existing.setStartDate(enc(newData.getStartDate()));

        if (newData.getEndDate() != null)
            existing.setEndDate(enc(newData.getEndDate()));

        if (newData.getDescription() != null)
            existing.setDescription(enc(newData.getDescription()));

        @SuppressWarnings("null")
		Experience updated = repo.save(existing);
        decrypt(updated);
        return updated;
    }

    private void encrypt(Experience e) {
        e.setCompany(enc(e.getCompany()));
        e.setRole(enc(e.getRole()));
        e.setStartDate(enc(e.getStartDate()));
        e.setEndDate(enc(e.getEndDate()));
        e.setDescription(enc(e.getDescription()));
    }

    private void decrypt(Experience e) {
        e.setCompany(dec(e.getCompany()));
        e.setRole(dec(e.getRole()));
        e.setStartDate(dec(e.getStartDate()));
        e.setEndDate(dec(e.getEndDate()));
        e.setDescription(dec(e.getDescription()));
    }

    private String enc(String data) {
        return data == null ? null : encryptionUtils.encrypt(data);
    }

    private String dec(String data) {
        return data == null ? null : encryptionUtils.decrypt(data);
    }

    public void delete(Long id) {
        if (id != null) {
            repo.deleteById(id);
        }
    }
}