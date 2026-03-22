package com.sarangsvkm.portfolio_api.apiuser;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



@Service
public interface ApiUserService {

	ApiUser create(ApiUser apiUser);

	ApiUser update(int userid, ApiUser apiUser);

	ResponseEntity<ApiUserResponse> deleteById(int userid);

	//ApiUser findByUsername(String decryptedValue);

	Optional<ApiUser> findById(int userId);

	ApiUser findByUsername(String username, String Password);

	

	
	

//	ApiUser authenticate(ApiUser user);
	

}
