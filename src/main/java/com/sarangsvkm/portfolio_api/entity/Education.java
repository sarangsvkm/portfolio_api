package com.sarangsvkm.portfolio_api.entity;



import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String degree;
    private String institution;
    private String year;
}