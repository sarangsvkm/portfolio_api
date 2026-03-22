package com.sarangsvkm.portfolio_api.dto;

import com.sarangsvkm.portfolio_api.entity.Experience;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExperienceRequest {

    private String username;
    private String password;
    private Experience experience;

    // getters
  
}