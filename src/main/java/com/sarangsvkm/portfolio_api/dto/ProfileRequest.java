package com.sarangsvkm.portfolio_api.dto;

import com.sarangsvkm.portfolio_api.entity.Profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileRequest {

    private String username;
    private String password;
    private Profile profile;
}