package com.sarangsvkm.portfolio_api.dto;

import com.sarangsvkm.portfolio_api.entity.Education;
import com.sarangsvkm.portfolio_api.entity.Experience;
import com.sarangsvkm.portfolio_api.entity.Profile;
import com.sarangsvkm.portfolio_api.entity.Project;
import com.sarangsvkm.portfolio_api.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDTO {

    private Profile profile;
    private List<Experience> experiences;
    private List<Education> educations;
    private List<Skill> skills;
    private List<Project> projects;
}
