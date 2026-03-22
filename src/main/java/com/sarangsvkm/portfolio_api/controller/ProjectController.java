package com.sarangsvkm.portfolio_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import com.sarangsvkm.portfolio_api.entity.Project;
import com.sarangsvkm.portfolio_api.service.ProjectService;
import com.sarangsvkm.portfolio_api.apiuser.ApiUserService;
import com.sarangsvkm.portfolio_api.apiuser.ApiUser;
import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService service;
    private final ApiUserService apiUserService;
    private final EncryptionUtils encryptionUtils;

    public ProjectController(ProjectService service, ApiUserService apiUserService, EncryptionUtils encryptionUtils) {
        this.service = service;
        this.apiUserService = apiUserService;
        this.encryptionUtils = encryptionUtils;
    }

    @PostMapping
    public Project save(@RequestHeader("username") String username,
            @RequestHeader("password") String password,
            @RequestBody Project p) throws Exception {

        String encryptedPassword = encryptionUtils.encrypt(password);
        ApiUser user = apiUserService.findByUsername(username, encryptedPassword);

        if (user == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED, "Invalid Credentials");
        }

        return service.save(p);
    }

    @GetMapping
    public List<Project> getAll() {
        return service.getAll();
    }
}