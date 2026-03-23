package com.sarangsvkm.portfolio_api.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Entity
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String name;

    @Column(length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String about;

    @Column(length = 500)
    private String email;

    @Column(length = 500)
    private String phone;

    @Column(length = 500)
    private String location;

    @Column(length = 1000)
    private String imageUrl;

    @Column(length = 1000)
    private String bannerUrl;

    @Column(length = 1000)
    private String resumeUrl;

    @Lob
    @Column(name = "profile_image")
    private byte[] profileImage;

    private String imageType;

    private String imageName;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SocialMedia> socialMediaLinks = new ArrayList<>();

}