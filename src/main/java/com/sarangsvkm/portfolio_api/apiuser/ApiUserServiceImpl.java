package com.sarangsvkm.portfolio_api.apiuser;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;

@Service
public class ApiUserServiceImpl implements ApiUserService {

    @Autowired
    ApiUserRepository apiUserRepository;

    @Autowired
    EncryptionUtils encryptionUtils;

    @Override
    public ApiUser create(ApiUser apiUser) {
        try {
            if (apiUser.getPassword() != null) {
                apiUser.setPassword(encryptionUtils.encrypt(apiUser.getPassword()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting password", e);
        }
        return apiUserRepository.save(apiUser);
    }

    @Override
    public ApiUser update(int userid, ApiUser apiUser) {
        ApiUser existingApiUser = apiUserRepository.findById(userid).orElse(null);
        if (existingApiUser != null) {
            if (apiUser.getUsername() != null) {
                existingApiUser.setUsername(apiUser.getUsername());
            }
            if (apiUser.getPassword() != null) {
                try {
                    existingApiUser.setPassword(encryptionUtils.encrypt(apiUser.getPassword()));
                } catch (Exception e) {
                    throw new RuntimeException("Error encrypting password", e);
                }
            }
            if (apiUser.getRole() != null) {
                existingApiUser.setRole(apiUser.getRole());
            }
            return apiUserRepository.save(existingApiUser);
        }
        return null;
    }

    @Override
    public ResponseEntity<ApiUserResponse> deleteById(int userid) {
        if (apiUserRepository.existsById(userid)) {
            apiUserRepository.deleteById(userid);
            String message = "ApiUser ID " + userid + " has been deleted successfully";
            return ResponseEntity.ok(new ApiUserResponse(null, message));
        } else {
            String message = "ApiUser with ID " + userid + " not found";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiUserResponse(null, message));
        }
    }

    @Override
    public ApiUser findByUsername(String username, String Password) {
        return apiUserRepository.findByUsername(username, Password);
    }

    public Optional<ApiUser> findById(int userId) {
        return apiUserRepository.findById(userId);
    }

}