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

    // 🔐 SAVE (Encrypt before DB)
    public Skill save(Skill s) {

        s.setName(enc(s.getName()));
        s.setLevel(enc(s.getLevel()));

        return repo.save(s);
    }

    // 🔓 GET ALL (Decrypt before response)
    public List<Skill> getAll() {

        List<Skill> list = repo.findAll();

        for (Skill s : list) {
            s.setName(dec(s.getName()));
            s.setLevel(dec(s.getLevel()));
        }

        return list;
    }

    // 🔧 Utility methods
    private String enc(String data) {
        if (data == null) return null;
        try {
            return encryptionUtils.encrypt(data);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    private String dec(String data) {
        if (data == null) return null;
        try {
            return encryptionUtils.decrypt(data);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}