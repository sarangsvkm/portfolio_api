package com.sarangsvkm.portfolio_api.apiuser;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;

@Service
public class ApiUserServiceImpl implements ApiUserService {

    private final ApiUserRepository repo;
    private final EncryptionUtils encryptionUtils;

    public ApiUserServiceImpl(ApiUserRepository repo, EncryptionUtils encryptionUtils) {
        this.repo = repo;
        this.encryptionUtils = encryptionUtils;
    }

    // ✅ REGISTER
    @Override
    public ApiUser register(ApiUser apiUser) {

        // 🔍 Check for duplicate username
        if (repo.findByUsername(apiUser.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        try {
            String encryptedPassword = encryptionUtils.encrypt(apiUser.getPassword());
            apiUser.setPassword(encryptedPassword);
            return repo.save(apiUser);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Encryption error");
        }
    }
    // ✅ LOGIN
    @Override
    public ApiUser login(String username, String password) {

        System.out.println("🔐 LOGIN ATTEMPT — username: [" + username + "]");

        ApiUser user = repo.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("❌ User not found: [" + username + "]");
                    return new RuntimeException("User not found");
                });

        System.out.println("✅ User found: [" + username + "]");

        String decryptedPassword = encryptionUtils.decrypt(user.getPassword());
        String inputPassword = password.trim();
        String dbPassword = decryptedPassword.trim();

        System.out.println("🔑 Input password:  [" + inputPassword + "]");
        System.out.println("🔑 DB password:     [" + dbPassword + "]");
        System.out.println("🔑 Match:           [" + inputPassword.equals(dbPassword) + "]");

        if (!inputPassword.equals(dbPassword)) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }

    // ✅ FIND BY ID
    @Override
    public Optional<ApiUser> findById(int userId) {
        return repo.findById(userId);
    }

    // ✅ DELETE
    @Override
    public void delete(int userId) {
        repo.deleteById(userId);
    }
}