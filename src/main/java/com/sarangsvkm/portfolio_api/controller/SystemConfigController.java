package com.sarangsvkm.portfolio_api.controller;

import com.sarangsvkm.portfolio_api.apiuser.ApiUserService;
import com.sarangsvkm.portfolio_api.entity.SystemConfig;
import com.sarangsvkm.portfolio_api.repository.SystemConfigRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/config")
public class SystemConfigController {

    private final SystemConfigRepository repository;
    private final ApiUserService apiUserService;

    public SystemConfigController(SystemConfigRepository repository, ApiUserService apiUserService) {
        this.repository = repository;
        this.apiUserService = apiUserService;
    }

    @GetMapping
    public ResponseEntity<List<SystemConfig>> getAllConfigs(
            @RequestHeader("username") String username,
            @RequestHeader("password") String password) {
        
        if (!apiUserService.login(username, password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<SystemConfig> saveOrUpdateConfig(
            @RequestHeader("username") String username,
            @RequestHeader("password") String password,
            @RequestBody SystemConfig config) {
        
        if (!apiUserService.login(username, password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<SystemConfig> existing = repository.findByConfigKey(config.getConfigKey());
        if (existing.isPresent()) {
            SystemConfig entry = existing.get();
            entry.setConfigValue(config.getConfigValue());
            return ResponseEntity.ok(repository.save(entry));
        }
        
        return ResponseEntity.ok(repository.save(config));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConfig(
            @RequestHeader("username") String username,
            @RequestHeader("password") String password,
            @PathVariable Long id) {
        
        if (!apiUserService.login(username, password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
