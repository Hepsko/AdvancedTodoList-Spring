package com.project.todolist.controller;


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

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;
    TaskController(final TaskRepository repository){
        this.repository=repository;
    }

    @GetMapping( params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks()
        {
            logger.warn("Exposing all the tasks!");
            return ResponseEntity.ok(repository.findAll());
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
