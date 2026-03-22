package com.sarangsvkm.portfolio_api.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.sarangsvkm.portfolio_api.entity.Profile;


public interface ProfileRepository extends JpaRepository<Profile, Long> {}