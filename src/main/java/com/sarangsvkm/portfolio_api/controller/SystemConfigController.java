package com.sarangsvkm.portfolio_api.controller;

import com.sarangsvkm.portfolio_api.apiuser.ApiUserService;
import com.sarangsvkm.portfolio_api.entity.SystemConfig;
import com.sarangsvkm.portfolio_api.repository.SystemConfigRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;

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
    public ResponseEntity<?> getAllConfigs(
            @RequestHeader("username") String username,
            @RequestHeader("password") String password) {
        
        try {
            apiUserService.login(username, password);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> saveOrUpdateConfig(
            @RequestHeader("username") String username,
            @RequestHeader("password") String password,
            @RequestBody SystemConfig config) {
        
        try {
            apiUserService.login(username, password);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
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
    public ResponseEntity<?> deleteConfig(
            @RequestHeader("username") String username,
            @RequestHeader("password") String password,
            @PathVariable Long id) {
        
        try {
            apiUserService.login(username, password);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
