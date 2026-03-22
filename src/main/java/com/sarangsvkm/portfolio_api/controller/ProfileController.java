package com.sarangsvkm.portfolio_api.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.sarangsvkm.portfolio_api.entity.Profile;
import com.sarangsvkm.portfolio_api.service.ProfileService;
import com.sarangsvkm.portfolio_api.apiuser.ApiUserService;
import com.sarangsvkm.portfolio_api.apiuser.ApiUser;
import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin
public class ProfileController {

    private final ProfileService service;
    private final ApiUserService apiUserService;
    private final EncryptionUtils encryptionUtils;

    public ProfileController(ProfileService service, ApiUserService apiUserService, EncryptionUtils encryptionUtils) {
        this.service = service;
        this.apiUserService = apiUserService;
        this.encryptionUtils = encryptionUtils;
    }

    // POST (Encrypt save)
    @PostMapping
    public Profile create(@RequestHeader("username") String username,
            @RequestHeader("password") String password,
            @RequestBody Profile p) throws Exception {

        String encryptedPassword = encryptionUtils.encrypt(password);
        ApiUser user = apiUserService.findByUsername(username, encryptedPassword);

        if (user == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED, "Invalid Credentials");
        }

        return service.save(p);
    }

    // GET (Decrypt output)
    @GetMapping
    public List<Profile> getAll() throws Exception {
        return service.getAll();
    }
}