package com.sarangsvkm.portfolio_api.apiuser;

import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sarangsvkm.portfolio_api.encryptionUtils.EncryptionUtils;




@RestController
@RequestMapping("/apiuser")
public class ApiUserController {
	
	 private final ApiUserService apiUserService;
	    private final EncryptionUtils encryptionUtils;

	    
	    public ApiUserController(ApiUserService apiUserService, EncryptionUtils encryptionUtils) {
	        this.apiUserService = apiUserService;
	        this.encryptionUtils = encryptionUtils;
	    }

	    @PostMapping("/create")
	    public ApiUser create(@RequestBody ApiUser apiUser) throws Exception {
	        if (apiUser.getUsername() == null) {
	            apiUser.setUsername("");
	        }
	        if (apiUser.getPassword() == null) {
	            apiUser.setPassword("");
	        }
	        if (apiUser.getRole() == null) {
	            apiUser.setRole("");
	        }

	        // Encrypt the username and password fields
	        String encryptedUsername = encryptionUtils.encrypt(apiUser.getUsername());
	        apiUser.setUsername(encryptedUsername);

	        String encryptedPassword = encryptionUtils.encrypt(apiUser.getPassword());
	        apiUser.setPassword(encryptedPassword);

	        // Save the encrypted values and decrypted role to the database
	        return apiUserService.create(apiUser);
	}
	
	    @PutMapping("/update/{userid}")
	    public ResponseEntity<ApiUser> update(@PathVariable("userid") int userId, @RequestBody ApiUser apiUser) throws Exception {
	        Optional<ApiUser> optionalExistingApiUser = apiUserService.findById(userId);
	        ApiUser existingApiUser = optionalExistingApiUser.orElse(null);

	        if (existingApiUser == null) {
	            return ResponseEntity.notFound().build();
	        }

	        // Update the encrypted fields if they are present in the request
	        if (apiUser.getUsername() != null) {
	            String encryptedUsername = encryptionUtils.encrypt(apiUser.getUsername());
	            existingApiUser.setUsername(encryptedUsername);
	        }
	        if (apiUser.getPassword() != null) {
	            String encryptedPassword = encryptionUtils.encrypt(apiUser.getPassword());
	            existingApiUser.setPassword(encryptedPassword);
	        }

	        // Update the remaining fields without encryption
	        if (apiUser.getRole() != null) {
	            existingApiUser.setRole(apiUser.getRole());
	        }

	        ApiUser updatedApiUser = apiUserService.update(userId, existingApiUser);

	        if (updatedApiUser != null) {
	            return ResponseEntity.ok(updatedApiUser);
	        } else {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }
	    
	    @GetMapping("/find-by-id/{userid}")
	    public ApiUser findById(@PathVariable("userid") int userId) throws Exception {
	        Optional<ApiUser> optionalApiUser = apiUserService.findById(userId);
	        ApiUser apiUser = optionalApiUser.orElseThrow(NotFoundException::new);

	        String decryptedUsername = encryptionUtils.decrypt(apiUser.getUsername());
	        apiUser.setUsername(decryptedUsername);

	        String decryptedPassword = encryptionUtils.decrypt(apiUser.getPassword());
	        apiUser.setPassword(decryptedPassword);

	        return apiUser;
	    }
	    
    
	@DeleteMapping("delete/{userid}")
	public ResponseEntity<ApiUserResponse> deleteKurytranById(@PathVariable int userid) {

        return apiUserService.deleteById(userid);

     }

}
