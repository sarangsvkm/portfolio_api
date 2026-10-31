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
    private final org.springframework.context.ApplicationEventPublisher eventPublisher;

    public EducationService(EducationRepository repo, EncryptionUtils encryptionUtils, org.springframework.context.ApplicationEventPublisher eventPublisher) {
        this.repo = repo;
        this.encryptionUtils = encryptionUtils;
        this.eventPublisher = eventPublisher;
    }

    public Education save(Education e) {
        if (e == null) throw new IllegalArgumentException("Education cannot be null");
        encrypt(e);
        Education saved = repo.save(e);
        decrypt(saved);
        eventPublisher.publishEvent(new com.sarangsvkm.portfolio_api.event.ResumeChangedEvent(this));
        return saved;
    }

    public List<Education> getAll() {
        List<Education> list = repo.findAll();
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

        if (newData.getDescription() != null)
            existing.setDescription(enc(newData.getDescription()));

		Education updated = repo.save(existing);
        decrypt(updated);
        eventPublisher.publishEvent(new com.sarangsvkm.portfolio_api.event.ResumeChangedEvent(this));
        return updated;
    }

    private void encrypt(Education e) {
        e.setInstitution(enc(e.getInstitution()));
        e.setDegree(enc(e.getDegree()));
        e.setFieldOfStudy(enc(e.getFieldOfStudy()));
        e.setStartDate(enc(e.getStartDate()));
        e.setEndDate(enc(e.getEndDate()));
        e.setDescription(enc(e.getDescription()));
    }

    private void decrypt(Education e) {
        e.setInstitution(dec(e.getInstitution()));
        e.setDegree(dec(e.getDegree()));
        e.setFieldOfStudy(dec(e.getFieldOfStudy()));
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