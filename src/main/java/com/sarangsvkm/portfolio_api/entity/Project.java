package com.sarangsvkm.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String title;

    @Column(length = 500)
    private String link;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String techStack;

    @Column(length = 100)
    private String projectType;

    @Column(length = 500)
    private String githubUrl;

    @Column(length = 500)
    private String imageUrl;
}
