package com.sarangsvkm.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;
    private String company;
    private String duration;

    @Column(length = 1000)
    private String description;
}