package com.sarangsvkm.portfolio_api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.sarangsvkm.portfolio_api.service.EducationService;
import com.sarangsvkm.portfolio_api.entity.Education;
import java.util.List;
import com.sarangsvkm.portfolio_api.apiuser.ApiUserService;
import com.sarangsvkm.portfolio_api.apiuser.ApiUser;
import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/education")
public class EducationController {

    private final EducationService service;
    private final ApiUserService apiUserService;
    private final EncryptionUtils encryptionUtils;

    public EducationController(EducationService service, ApiUserService apiUserService, EncryptionUtils encryptionUtils) {
        this.service = service;
        this.apiUserService = apiUserService;
        this.encryptionUtils = encryptionUtils;
    }

    @PostMapping
    public Education save(@RequestHeader("username") String username,
            @RequestHeader("password") String password,
            @RequestBody Education e) throws Exception {

        String encryptedPassword = encryptionUtils.encrypt(password);
        ApiUser user = apiUserService.findByUsername(username, encryptedPassword);

        if (user == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED, "Invalid Credentials");
        }

        return service.save(e);
    }

    @GetMapping
    public List<Education> getAll() {
        return service.getAll();
    }
}