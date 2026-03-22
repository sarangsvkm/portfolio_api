package com.sarangsvkm.portfolio_api.service;



import org.springframework.stereotype.Service;
import java.util.List;

import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;
import com.sarangsvkm.portfolio_api.entity.Project;
import com.sarangsvkm.portfolio_api.repository.ProjectRepository;

@Service
public class ProjectService {

    private final ProjectRepository repo;
    private final EncryptionUtils encryptionUtils;

    public ProjectService(ProjectRepository repo, EncryptionUtils encryptionUtils) {
        this.repo = repo;
        this.encryptionUtils = encryptionUtils;
    }

    // 🔐 SAVE
    public Project save(Project p) {

        p.setName(enc(p.getName()));
        p.setDescription(enc(p.getDescription()));
        p.setTechStack(enc(p.getTechStack()));

        return repo.save(p);
    }

    // 🔓 GET
    public List<Project> getAll() {

        List<Project> list = repo.findAll();

        for (Project p : list) {
            p.setName(dec(p.getName()));
            p.setDescription(dec(p.getDescription()));
            p.setTechStack(dec(p.getTechStack()));
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