package com.sarangsvkm.portfolio_api.apiuser;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiUserRepository extends JpaRepository<ApiUser, Integer> {

    Optional<ApiUser> findByUsername(String username);
}