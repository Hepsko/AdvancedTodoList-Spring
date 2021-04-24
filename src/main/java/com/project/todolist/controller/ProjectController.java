package com.project.todolist.controller;

import com.project.todolist.logic.ProjectService;
import com.project.todolist.model.Project;
import com.project.todolist.model.ProjectStep;
import com.project.todolist.model.projection.ProjectWriteModel;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping
    String showProjects(Model model){
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
    }


    @PostMapping
    String addProject(@Valid @ModelAttribute("project") ProjectWriteModel current, BindingResult result, Model model){
        if(result.hasErrors()){
            return "projects";
        }

            service.save(current);
            model.addAttribute("project", new ProjectWriteModel());
            model.addAttribute("projects", getPtojects());
            model.addAttribute("message", "Dodano projekt");
            return "projects";


    }




    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current ){
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @PostMapping("/{id}")
    String createGroup(@ModelAttribute("project") ProjectWriteModel current,
                       Model model, @PathVariable int id, @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline){
        try {
            service.createGroup(id, deadline);
            model.addAttribute("message", "Dodano grupe");
        }
        catch (IllegalStateException | IllegalArgumentException e){
            model.addAttribute("message", "Błąd podczas tworzenia grupy");
        }
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getPtojects(){
       return service.readAll();
    }
}
