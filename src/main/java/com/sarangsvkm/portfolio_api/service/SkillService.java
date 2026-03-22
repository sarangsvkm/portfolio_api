package com.sarangsvkm.portfolio_api.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;
import com.sarangsvkm.portfolio_api.entity.Skill;
import com.sarangsvkm.portfolio_api.repository.SkillRepository;

@Service
public class SkillService {

    private final SkillRepository repo;
    private final EncryptionUtils encryptionUtils;

    public SkillService(SkillRepository repo, EncryptionUtils encryptionUtils) {
        this.repo = repo;
        this.encryptionUtils = encryptionUtils;
    }

    public Skill save(Skill s) {
        encrypt(s);
        Skill saved = repo.save(s);
        decrypt(saved);
        return saved;
    }

    public List<Skill> getAll() {
        List<Skill> list = repo.findAll();
        list.forEach(this::decrypt);
        return list;
    }

    public Skill update(int id, Skill newData) {
        Skill existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        if (newData.getName() != null)
            existing.setName(enc(newData.getName()));

        if (newData.getLevel() != null)
            existing.setLevel(enc(newData.getLevel()));

        if (newData.getCategory() != null)
            existing.setCategory(enc(newData.getCategory()));

        Skill updated = repo.save(existing);
        decrypt(updated);
        return updated;
    }

    private void encrypt(Skill s) {
        s.setName(enc(s.getName()));
        s.setLevel(enc(s.getLevel()));
        s.setCategory(enc(s.getCategory()));
    }

    private void decrypt(Skill s) {
        s.setName(dec(s.getName()));
        s.setLevel(dec(s.getLevel()));
        s.setCategory(dec(s.getCategory()));
    }

    private String enc(String data) {
        return data == null ? null : encryptionUtils.encrypt(data);
    }

    private String dec(String data) {
        return data == null ? null : encryptionUtils.decrypt(data);
    }
}