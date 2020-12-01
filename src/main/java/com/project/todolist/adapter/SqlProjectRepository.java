package com.project.todolist.adapter;

import com.project.todolist.model.Project;
import com.project.todolist.model.ProjectRepository;
import com.project.todolist.model.TaskGroup;
import com.project.todolist.model.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {

    @Override
    @Query("from Project p join fetch p.steps")
    List<Project> findAll();




}
