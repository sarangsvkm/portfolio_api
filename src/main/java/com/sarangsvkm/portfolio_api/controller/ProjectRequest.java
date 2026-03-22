

package com.sarangsvkm.portfolio_api.controller;
import com.sarangsvkm.portfolio_api.entity.Project;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class ProjectRequest {

    private String username;
    private String password;
    private Project project;
}