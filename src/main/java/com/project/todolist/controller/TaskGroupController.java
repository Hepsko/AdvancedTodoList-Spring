package com.project.todolist.controller;


import com.project.todolist.logic.TaksGroupService;
import com.project.todolist.logic.TaskService;
import com.project.todolist.model.Task;
import com.project.todolist.model.TaskGroup;
import com.project.todolist.model.TaskRepository;
import com.project.todolist.model.projection.GroupReadModel;
import com.project.todolist.model.projection.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/groups")
public class TaskGroupController {
    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskRepository repository;
    private final TaksGroupService service;
    TaskGroupController(final TaskRepository repository, TaksGroupService service){
        this.repository=repository;
        this.service = service;
    }

    @GetMapping
   ResponseEntity<List<GroupReadModel>> readAllGroups()
        {
            return ResponseEntity.ok(service.readAll());
        }

    @GetMapping("/{id}")
    ResponseEntity<List<Task>> readAllTaskFromGroup(@PathVariable("id") int id){
        return ResponseEntity.ok(repository.findAllByGroup_Id(id));
    }

    @PostMapping
    ResponseEntity<?> createGroup( @RequestBody @Valid GroupWriteModel toCreate)
    {
        GroupReadModel result =  service.createGroup(toCreate);
    return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id)
    {
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e)
    {
        return  ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalState(IllegalStateException e)
    {
        return  ResponseEntity.badRequest().body(e.getMessage());
    }

}
