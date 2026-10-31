package com.sarangsvkm.portfolio_api.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;
import com.sarangsvkm.portfolio_api.entity.Experience;
import com.sarangsvkm.portfolio_api.repository.ExperienceRepository;

@Service
@SuppressWarnings("null")
public class ExperienceService {

    private final ExperienceRepository repo;
    private final EncryptionUtils encryptionUtils;
    private final org.springframework.context.ApplicationEventPublisher eventPublisher;

    public ExperienceService(ExperienceRepository repo, EncryptionUtils encryptionUtils, org.springframework.context.ApplicationEventPublisher eventPublisher) {
        this.repo = repo;
        this.encryptionUtils = encryptionUtils;
        this.eventPublisher = eventPublisher;
    }

    public Experience save(Experience e) {
        if (e == null) throw new IllegalArgumentException("Experience cannot be null");
        encrypt(e);
        Experience saved = repo.save(e);
        decrypt(saved);
        eventPublisher.publishEvent(new com.sarangsvkm.portfolio_api.event.ResumeChangedEvent(this));
        return saved;
    }

    public List<Experience> getAll() {
        List<Experience> list = repo.findAll();
        list.forEach(this::decrypt);
        list.sort((a, b) -> {
            int scoreB = parseDateScore(b.getEndDate(), b.getStartDate());
            int scoreA = parseDateScore(a.getEndDate(), a.getStartDate());
            return Integer.compare(scoreB, scoreA);
        });
        return list;
    }

    private int parseDateScore(String endDate, String startDate) {
        int endScore = parseSingleDateScore(endDate);
        int startScore = parseSingleDateScore(startDate);
        return endScore > 0 ? endScore : startScore;
    }

    private int parseSingleDateScore(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return 0;
        String s = dateStr.trim();
        if (s.equalsIgnoreCase("present")) return 999999;

        int year = 0;
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\b(19\\d{2}|20\\d{2})\\b").matcher(s);
        if (m.find()) {
            year = Integer.parseInt(m.group(1));
        }

        int month = 0;
        String lower = s.toLowerCase();
        String[] months = {"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};
        for (int i = 0; i < months.length; i++) {
            if (lower.contains(months[i])) {
                month = i + 1;
                break;
            }
        }

        return year * 100 + month;
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
        eventPublisher.publishEvent(new com.sarangsvkm.portfolio_api.event.ResumeChangedEvent(this));
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
            eventPublisher.publishEvent(new com.sarangsvkm.portfolio_api.event.ResumeChangedEvent(this));
        }
    }
}