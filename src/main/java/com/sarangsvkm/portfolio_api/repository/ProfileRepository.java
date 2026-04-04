package com.sarangsvkm.portfolio_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sarangsvkm.portfolio_api.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @EntityGraph(attributePaths = "socialMediaLinks")
    List<Profile> findAll();

    @EntityGraph(attributePaths = "socialMediaLinks")
    Optional<Profile> findById(Long id);
}
