package com.project.todolist.logic;


import com.project.todolist.model.Task;
import com.project.todolist.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Service
public class TaskService {
    private final TaskRepository repository;


    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    @Async
    public CompletableFuture<List<Task>> findAllAsync(){
        return CompletableFuture.supplyAsync(
                new Supplier<List<Task>>() {
                    @Override
                    public List<Task> get() {
                        return  repository.findAll();
                    }
                }
        );
    }
}
