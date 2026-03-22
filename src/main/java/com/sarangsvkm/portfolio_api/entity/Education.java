package com.sarangsvkm.portfolio_api.entity;



import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String degree;

    @Column(length = 500)
    private String institution;

    @Column(length = 500)
    private String fieldOfStudy;

    @Column(length = 500)
    private String startDate;

    @Column(length = 500)
    private String endDate;

	
}