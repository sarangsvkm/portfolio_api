package com.sarangsvkm.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Data;

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

}