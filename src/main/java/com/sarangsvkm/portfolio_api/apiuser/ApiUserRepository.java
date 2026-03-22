package com.sarangsvkm.portfolio_api.apiuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ApiUserRepository extends JpaRepository<ApiUser, Integer> {

	@Query("Select k from ApiUser k where k.username=:username AND k.password=:password")
	ApiUser findByUsername(String username, String password);

	

}
