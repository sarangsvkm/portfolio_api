package com.sarangsvkm.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data


public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String role;

    @Column(length = 500)
    private String company;

    @Column(length = 500)
    private String startDate;

    @Column(length = 500)
    private String endDate;

    @Column(columnDefinition = "TEXT")
    private String description;

}