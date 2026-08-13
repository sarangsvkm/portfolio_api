package com.sarangsvkm.portfolio_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarangsvkm.portfolio_api.dto.ResumeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ResumeJsonService {

    private static final Logger log = LoggerFactory.getLogger(ResumeJsonService.class);

    private final ResumeService resumeService;
    private final ObjectMapper objectMapper;

    @Value("${resume.json.output-path:}")
    private String jsonOutputPath;

    // In-memory cache for the public resume
    private volatile ResumeDTO cachedPublicResume;

    public ResumeJsonService(ResumeService resumeService, ObjectMapper objectMapper) {
        this.resumeService = resumeService;
        this.objectMapper = objectMapper;
    }

    /**
     * Initializes the cache on application startup.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initializeCache() {
        log.info("Initializing Resume Cache...");
        updateCache();
    }

    /**
     * Listens to ResumeChangedEvent and updates the cache.
     */
    @EventListener(com.sarangsvkm.portfolio_api.event.ResumeChangedEvent.class)
    public void onResumeChanged(com.sarangsvkm.portfolio_api.event.ResumeChangedEvent event) {
        log.info("Received ResumeChangedEvent from source: {}", event.getSource().getClass().getSimpleName());
        updateCache();
    }

    /**
     * Re-fetches the latest resume data from PostgreSQL, updates the in-memory cache,
     * and writes it to the static JSON file.
     */
    public synchronized void updateCache() {
        try {
            log.info("Updating resume cache from PostgreSQL database...");
            // We cache the redacted/public version of the resume
            ResumeDTO freshResume = resumeService.getResume(true);
            this.cachedPublicResume = freshResume;

            writeJsonFile(freshResume);
            log.info("Resume cache successfully updated.");
        } catch (Exception e) {
            log.error("Failed to update resume cache", e);
        }
    }

    /**
     * Returns the cached public resume. If the cache is empty, it triggers an update.
     */
    public ResumeDTO getCachedResume() {
        if (cachedPublicResume == null) {
            updateCache();
        }
        return cachedPublicResume;
    }

    /**
     * Writes the ResumeDTO to a JSON file on the disk if output path is configured.
     */
    private void writeJsonFile(ResumeDTO resume) {
        if (jsonOutputPath == null || jsonOutputPath.trim().isEmpty()) {
            log.info("No resume JSON output path configured; skipping file generation.");
            return;
        }

        try {
            String jsonContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(resume);
            Path path = Paths.get(jsonOutputPath).toAbsolutePath().normalize();
            
            // Ensure parent directories exist
            File parentDir = path.getParent().toFile();
            if (!parentDir.exists()) {
                boolean created = parentDir.mkdirs();
                if (created) {
                    log.info("Created parent directories for JSON path: {}", parentDir);
                }
            }

            Files.writeString(path, jsonContent);
            log.info("Successfully wrote resume-data.json to: {}", path);
        } catch (IOException e) {
            log.error("Failed to write resume JSON to file: {}", jsonOutputPath, e);
        }
    }
}
