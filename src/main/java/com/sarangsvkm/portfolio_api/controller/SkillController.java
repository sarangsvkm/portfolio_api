package com.sarangsvkm.portfolio_api.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.sarangsvkm.portfolio_api.entity.Skill;
import com.sarangsvkm.portfolio_api.service.SkillService;
import com.sarangsvkm.portfolio_api.apiuser.ApiUserService;
import com.sarangsvkm.portfolio_api.apiuser.ApiUser;
import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin
public class SkillController {

    private final SkillService service;
    private final ApiUserService apiUserService;
    private final EncryptionUtils encryptionUtils;

    public SkillController(SkillService service, ApiUserService apiUserService, EncryptionUtils encryptionUtils) {
        this.service = service;
        this.apiUserService = apiUserService;
        this.encryptionUtils = encryptionUtils;
    }

    // POST
    @PostMapping
    public Skill addSkill(@RequestHeader("username") String username,
            @RequestHeader("password") String password,
            @RequestBody Skill s) throws Exception {

        String encryptedPassword = encryptionUtils.encrypt(password);
        ApiUser user = apiUserService.findByUsername(username, encryptedPassword);

        if (user == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED, "Invalid Credentials");
        }

        return service.save(s);
    }

    // GET
    @GetMapping
    public List<Skill> getSkills() {
        return service.getAll();
    }
}