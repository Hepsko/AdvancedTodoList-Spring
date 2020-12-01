package com.project.todolist.model;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    List<Project> findAll();

    Optional<Project> findById(Integer i);

    Project save(Project entity);



}
