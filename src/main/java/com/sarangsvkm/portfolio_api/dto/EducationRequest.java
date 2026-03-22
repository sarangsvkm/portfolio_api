package com.sarangsvkm.portfolio_api.dto;

import com.sarangsvkm.portfolio_api.entity.Education;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EducationRequest {

    private String username;
    private String password;
    private Education education;
}