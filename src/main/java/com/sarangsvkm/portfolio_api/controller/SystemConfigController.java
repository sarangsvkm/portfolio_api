package com.sarangsvkm.portfolio_api.controller;

import com.sarangsvkm.portfolio_api.entity.SystemConfig;
import com.sarangsvkm.portfolio_api.repository.SystemConfigRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/config")
public class SystemConfigController {

    private final SystemConfigRepository repository;

    public SystemConfigController(SystemConfigRepository repository) {
        this.repository = repository;
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
