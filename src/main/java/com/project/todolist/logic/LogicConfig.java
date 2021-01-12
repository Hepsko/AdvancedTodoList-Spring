package com.project.todolist.logic;


import com.project.todolist.TaskConfigurationProperties;
import com.project.todolist.model.ProjectRepository;
import com.project.todolist.model.TaskGroup;
import com.project.todolist.model.TaskGroupRepository;
import com.project.todolist.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogicConfig {
    @Bean
    ProjectService projectService(
        final ProjectRepository repository,
        final TaskGroupRepository taskGroupRepository,
        final TaksGroupService service,
        final TaskConfigurationProperties  config
    ){
        return new ProjectService(repository,taskGroupRepository,config, service);
    }

    @Bean
    TaksGroupService taksGroupService(
            TaskGroupRepository repository,
            TaskRepository taskRepository
    )
    {
        return new TaksGroupService(repository,taskRepository);
    }
}
