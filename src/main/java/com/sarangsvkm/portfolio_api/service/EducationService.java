package com.sarangsvkm.portfolio_api.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;
import com.sarangsvkm.portfolio_api.entity.Education;
import com.sarangsvkm.portfolio_api.repository.EducationRepository;


@Service
@SuppressWarnings("null")
public class EducationService {

    private final EducationRepository repo;
    private final EncryptionUtils encryptionUtils;

    public EducationService(EducationRepository repo, EncryptionUtils encryptionUtils) {
        this.repo = repo;
        this.encryptionUtils = encryptionUtils;
    }

    public Education save(Education e) {
        if (e == null) throw new IllegalArgumentException("Education cannot be null");
        encrypt(e);
        Education saved = repo.save(e);
        decrypt(saved);
        return saved;
    }

    public List<Education> getAll() {
        List<Education> list = repo.findAll();
        list.forEach(this::decrypt);
        return list;
    }

    public Education update(Long id, Education newData) {
        if (id == null) throw new IllegalArgumentException("ID cannot be null");
        Education existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Education not found"));

        if (newData.getInstitution() != null)
            existing.setInstitution(enc(newData.getInstitution()));

        if (newData.getDegree() != null)
            existing.setDegree(enc(newData.getDegree()));

        if (newData.getFieldOfStudy() != null)
            existing.setFieldOfStudy(enc(newData.getFieldOfStudy()));

        if (newData.getStartDate() != null)
            existing.setStartDate(enc(newData.getStartDate()));

        if (newData.getEndDate() != null)
            existing.setEndDate(enc(newData.getEndDate()));

  
		Education updated = repo.save(existing);
        decrypt(updated);
        return updated;
    }

    private void encrypt(Education e) {
        e.setInstitution(enc(e.getInstitution()));
        e.setDegree(enc(e.getDegree()));
        e.setFieldOfStudy(enc(e.getFieldOfStudy()));
        e.setStartDate(enc(e.getStartDate()));
        e.setEndDate(enc(e.getEndDate()));
    }

    private void decrypt(Education e) {
        e.setInstitution(dec(e.getInstitution()));
        e.setDegree(dec(e.getDegree()));
        e.setFieldOfStudy(dec(e.getFieldOfStudy()));
        e.setStartDate(dec(e.getStartDate()));
        e.setEndDate(dec(e.getEndDate()));
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