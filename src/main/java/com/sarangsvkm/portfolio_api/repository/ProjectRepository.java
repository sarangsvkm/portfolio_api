package com.sarangsvkm.portfolio_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sarangsvkm.portfolio_api.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {}