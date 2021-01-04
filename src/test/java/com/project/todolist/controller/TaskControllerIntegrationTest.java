package com.project.todolist.controller;


import com.project.todolist.model.Task;
import com.project.todolist.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class TaskControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    TaskRepository repo;

     @Test
    void http_returnGivenTask() throws Exception {
        int id = repo.save(new Task("foo", LocalDateTime.now())).getId();

         mockMvc.perform((get("/tasks/" +id))).andExpect(status().is2xxSuccessful());
     }

}
