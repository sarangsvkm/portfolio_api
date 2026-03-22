package com.sarangsvkm.portfolio_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sarangsvkm.portfolio_api.entity.Experience;
import com.sarangsvkm.portfolio_api.service.ExperienceService;
import com.sarangsvkm.portfolio_api.apiuser.ApiUserService;
import com.sarangsvkm.portfolio_api.apiuser.ApiUser;
import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/experience")
public class ExperienceController {

    private final ExperienceService service;
    private final ApiUserService apiUserService;
    private final EncryptionUtils encryptionUtils;

    public ExperienceController(ExperienceService service, ApiUserService apiUserService, EncryptionUtils encryptionUtils) {
        this.service = service;
        this.apiUserService = apiUserService;
        this.encryptionUtils = encryptionUtils;
    }

    @PostMapping
    public Experience save(@RequestHeader("username") String username,
            @RequestHeader("password") String password,
            @RequestBody Experience e) throws Exception {

        String encryptedPassword = encryptionUtils.encrypt(password);
        ApiUser user = apiUserService.findByUsername(username, encryptedPassword);

        if (user == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED, "Invalid Credentials");
        }

        return service.save(e);
    }

    @GetMapping
    public List<Experience> getAll() {
        return service.getAll();
    }
}