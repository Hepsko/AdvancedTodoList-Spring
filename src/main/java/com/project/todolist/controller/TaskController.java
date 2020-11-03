package com.project.todolist.controller;


import com.project.todolist.model.TaskRepository;
import org.aspectj.apache.bcel.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RepositoryRestController
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;
    TaskController(final TaskRepository repository){
        this.repository=repository;
    }

    @GetMapping(value = "/tasks")
    ResponseEntity<?> readAllTasks()
        {
            logger.warn("Exposing all the tasks!");
            return ResponseEntity.ok(repository.findAll());
        }

}
