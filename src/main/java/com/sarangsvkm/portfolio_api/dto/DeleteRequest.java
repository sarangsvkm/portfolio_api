package com.sarangsvkm.portfolio_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteRequest {
    private String username;
    private String password;
}
