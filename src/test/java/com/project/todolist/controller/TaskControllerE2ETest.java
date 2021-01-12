package com.project.todolist.controller;

import com.project.todolist.model.Task;
import com.project.todolist.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import java.time.LocalDateTime;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest {
@LocalServerPort
    private int port;

@Autowired
    private TestRestTemplate restTemplate;

@Autowired
    TaskRepository repo;

@Test
    void httpGet_returnsAllTasks()
{
    int value = repo.findAll().size();
    repo.save(new Task("foo", LocalDateTime.now()));
    repo.save(new Task("bar", LocalDateTime.now()));

    Task result[] =restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);

    assertThat(result).hasSize(2+value);
}


}