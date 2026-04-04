package com.sarangsvkm.portfolio_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sarangsvkm.portfolio_api.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteByProfileId(Long profileId);
    Image findByProfileId(Long profileId);
    Image findFirstByNameIgnoreCase(String name);
}
