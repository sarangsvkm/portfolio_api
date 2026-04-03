package com.sarangsvkm.portfolio_api.controller;

import com.sarangsvkm.portfolio_api.entity.SystemConfig;
import com.sarangsvkm.portfolio_api.repository.SystemConfigRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/config")
public class SystemConfigController {

    private final SystemConfigRepository repository;

    @Value("${application.version:0.0.0}")
    private String appVersion;

    public SystemConfigController(SystemConfigRepository repository) {
        this.repository = repository;
    }

    // ✅ GET VERSION
    @GetMapping("/version")
    public ResponseEntity<Map<String, String>> getVersion() {
        Map<String, String> response = new HashMap<>();
        response.put("version", appVersion);
        return ResponseEntity.ok(response);
    }

    // ✅ GET ALL (Auth handled by Filter)
    @GetMapping
    public ResponseEntity<List<SystemConfig>> getAllConfigs() {
        return ResponseEntity.ok(repository.findAll());
    }

    // ✅ SAVE/UPDATE (Auth handled by Filter)
    @PostMapping
    public ResponseEntity<SystemConfig> saveOrUpdateConfig(@RequestBody SystemConfig config) {
        Optional<SystemConfig> existing = repository.findByConfigKey(config.getConfigKey());
        if (existing.isPresent()) {
            SystemConfig entry = existing.get();
            entry.setConfigValue(config.getConfigValue());
            return ResponseEntity.ok(repository.save(entry));
        }
        return ResponseEntity.ok(repository.save(config));
    }

    // ✅ DELETE (Auth handled by Filter)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConfig(@PathVariable Long id) {
        if (id != null) {
            repository.deleteById(id);
        }
        return ResponseEntity.noContent().build();
    }
}
