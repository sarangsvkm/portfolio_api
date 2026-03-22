package com.sarangsvkm.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String title;
    private String about;
    private String email;
    private String phone;
    private String location;
}