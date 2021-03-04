package com.project.todolist.logic;

import com.project.todolist.TaskConfigurationProperties;
import com.project.todolist.model.*;

import com.project.todolist.model.projection.GroupReadModel;
import com.project.todolist.model.projection.GroupTaskWriteModel;
import com.project.todolist.model.projection.GroupWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class ProjectService {
private ProjectRepository repository;
private TaskGroupRepository taskGroupRepository;
private TaskConfigurationProperties config;
private  TaksGroupService service;

ProjectService(final ProjectRepository repository, final TaskGroupRepository taskGroupRepository, final TaskConfigurationProperties config, TaksGroupService service)
{
    this.repository=repository;
    this.config=config;
    this.taskGroupRepository=taskGroupRepository;

    this.service = service;
}

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save( final Project toSave)
    {
        return repository.save(toSave);
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline)
    {
        if(!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProjectId(projectId))
        {
            throw  new IllegalStateException("Only one undone group from project is allowed ");
        }
        GroupReadModel result= repository.findById(projectId).map(project -> {

           var taskGroup = new GroupWriteModel();
           taskGroup.setDescription(project.getDescription());
           taskGroup.setTasks(project.getSteps().stream().map(projectStep -> {
               var task = new GroupTaskWriteModel();
               task.setDescription(projectStep.getDescription());
               task.setDeadline( deadline.plusDays(projectStep.getDaysToDeadline()));
               return task;
                   }).collect(Collectors.toSet()));
         return   service.createGroup(taskGroup);
        }).orElseThrow(() -> new IllegalArgumentException("Project not found"));

             return result;
    }
}
