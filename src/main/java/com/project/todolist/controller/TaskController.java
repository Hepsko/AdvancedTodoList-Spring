package com.project.todolist.controller;


import com.project.todolist.logic.TaskService;
import com.project.todolist.model.Task;
import com.project.todolist.model.TaskRepository;
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
@RequestMapping("/tasks")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;
    private final TaskService service;
    TaskController(final TaskRepository repository, TaskService service){
        this.repository=repository;
        this.service = service;
    }

    @GetMapping( params = {"!sort", "!page", "!size"})
    CompletableFuture<ResponseEntity<List<Task>>> readAllTasks()
        {
            return service.findAllAsync().thenApply(ResponseEntity::ok);
        }


    @GetMapping
    ResponseEntity<?> readAllTasks(Pageable page)
    {
        logger.info("Custrom pegeable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateTask(@PathVariable("id") int taskId,  @RequestBody @Valid  Task toUpdate)
    {
        if(!repository.existsById(taskId)) {
          return  ResponseEntity.notFound().build();
        }
        repository.findById(taskId).ifPresent(task ->
        {
            task.updateFrom(toUpdate);
            repository.save(task);
        });
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getTask(@PathVariable("id") int taskId)
    {
        return repository.findById(taskId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<?> createTask( @RequestBody @Valid  Task toCreate)
    {
    Task result = repository.save(toCreate);
    return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable("id") int taskId)
    {
        if(!repository.existsById(taskId)) {
            return  ResponseEntity.notFound().build();
        }

        repository.findById(taskId).ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("search/done")
    ResponseEntity<List<Task>> ReadDoneTask(@RequestParam(defaultValue = "true")   boolean state) {

        return ResponseEntity.ok(repository.findByDone(state));

    }


}
