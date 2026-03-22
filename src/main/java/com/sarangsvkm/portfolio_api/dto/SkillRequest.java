package com.sarangsvkm.portfolio_api.dto;

import com.sarangsvkm.portfolio_api.entity.Skill;

public class SkillRequest {

    private String username;
    private String password;
    private Skill skill;

    // getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Skill getSkill() { return skill; }

    // setters
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setSkill(Skill skill) { this.skill = skill; }
}