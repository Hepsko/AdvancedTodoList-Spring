package com.project.todolist.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@RequestMapping("/projects")
public class ProjectController {
    @GetMapping
    String showProjects(){
        return "projects";
    }
}
