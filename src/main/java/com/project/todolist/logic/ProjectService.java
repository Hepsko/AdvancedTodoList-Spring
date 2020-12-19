package com.project.todolist.logic;

import com.project.todolist.TaskConfigurationProperties;
import com.project.todolist.model.*;

import com.project.todolist.model.projection.GroupReadModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
private ProjectRepository repository;
private TaskGroupRepository taskGroupRepository;
private TaskConfigurationProperties config;
ProjectService(final ProjectRepository repository, final TaskGroupRepository taskGroupRepository, final TaskConfigurationProperties config  )
{
    this.repository=repository;
    this.config=config;
    this.taskGroupRepository=taskGroupRepository;
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
        if(!config.getTemplate().isAllowallowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProjectId(projectId))
        {
            throw  new IllegalStateException("Only one undone group from project is allowed ");
        }
       TaskGroup result= repository.findById(projectId).map(project -> {
            var taskGroup = new TaskGroup();
            taskGroup.setDescription(project.getDescription());
            taskGroup.setTasks(project.getSteps().stream().map
                    (
                            projectStep ->  new Task(
                                    projectStep.getDescription(),
                                    deadline.plusDays(projectStep.getDaysToDeadline())
                            )
                    ).collect(Collectors.toSet()));
                return taskGroup;
        }).orElseThrow(() -> new IllegalArgumentException("Project not found"));

             return  new GroupReadModel(result);
    }
}
