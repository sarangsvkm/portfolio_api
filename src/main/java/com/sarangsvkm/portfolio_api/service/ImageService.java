package com.sarangsvkm.portfolio_api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sarangsvkm.portfolio_api.entity.Image;
import com.sarangsvkm.portfolio_api.repository.ImageRepository;

@Service
@SuppressWarnings("null")
public class ImageService {

    private final ImageRepository repo;

    public ImageService(ImageRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Image save(Image img) {
        return repo.save(img);
    }

    @Transactional(readOnly = true)
    public Image findByProfileId(Long profileId) {
        return repo.findByProfileId(profileId);
    }

    @Transactional(readOnly = true)
    public Image findByName(String name) {
        return repo.findFirstByNameIgnoreCase(name);
    }

    @Transactional
    public void deleteByProfileId(Long profileId) {
        repo.deleteByProfileId(profileId);
    }
}
