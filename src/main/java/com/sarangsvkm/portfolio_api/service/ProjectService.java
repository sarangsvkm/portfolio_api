package com.sarangsvkm.portfolio_api.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;
import com.sarangsvkm.portfolio_api.entity.Project;
import com.sarangsvkm.portfolio_api.repository.ProjectRepository;

@Service
@SuppressWarnings("null")
public class ProjectService {

    private final ProjectRepository repo;
    private final EncryptionUtils encryptionUtils;

    public ProjectService(ProjectRepository repo, EncryptionUtils encryptionUtils) {
        this.repo = repo;
        this.encryptionUtils = encryptionUtils;
    }

    public Project save(Project p) {
        if (p == null) throw new IllegalArgumentException("Project cannot be null");
        encrypt(p);
        Project saved = repo.save(p);
        decrypt(saved);
        return saved;
    }

    public List<Project> getAll() {
        List<Project> list = repo.findAll();
        list.forEach(this::decrypt);
        return list;
    }

    public Project update(Long id, Project newData) {
        if (id == null) throw new IllegalArgumentException("ID cannot be null");
        Project existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (newData.getTitle() != null)
            existing.setTitle(enc(newData.getTitle()));

        if (newData.getLink() != null)
            existing.setLink(enc(newData.getLink()));

        if (newData.getDescription() != null)
            existing.setDescription(enc(newData.getDescription()));
            
        if (newData.getTechStack() != null)
            existing.setTechStack(enc(newData.getTechStack()));

        Project updated = repo.save(existing);
        decrypt(updated);
        return updated;
    }

    private void encrypt(Project p) {
        p.setTitle(enc(p.getTitle()));
        p.setLink(enc(p.getLink()));
        p.setDescription(enc(p.getDescription()));
        p.setTechStack(enc(p.getTechStack()));
    }

    private void decrypt(Project p) {
        p.setTitle(dec(p.getTitle()));
        p.setLink(dec(p.getLink()));
        p.setDescription(dec(p.getDescription()));
        p.setTechStack(dec(p.getTechStack()));
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