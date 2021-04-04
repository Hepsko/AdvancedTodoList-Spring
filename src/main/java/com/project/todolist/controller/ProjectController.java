package com.project.todolist.controller;

import com.project.todolist.model.ProjectStep;
import com.project.todolist.model.projection.ProjectWriteModel;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Component
@RequestMapping("/projects")
public class ProjectController {
    @GetMapping
    String showProjects(Model model){
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current ){
        current.getSteps().add(new ProjectStep());
        return "projects";
    }
}
