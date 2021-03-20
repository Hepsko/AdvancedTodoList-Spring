package com.project.todolist;

import com.project.todolist.model.Task;
import com.project.todolist.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.DriverManager;
import java.util.*;

@Configuration
public class TestConfiguration {
    @Bean
    @Primary
    @Profile("!integration")
    DataSource e2eTestDatasource()
    {
        var result = new DriverManagerDataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1","sa", "");
        result.setDriverClassName("org.h2.Driver");

        return result;
    }

    @Bean
    @Primary
    @Profile("integration")
  public   TaskRepository taskRepo()
    {
        return new TaskRepository() {
            private Map<Integer, Task> tasks = new HashMap<>();
            @Override
            public List<Task> findAll() {
                return new ArrayList<>(tasks.values());
            }

            @Override
            public Page<Task> findAll(Pageable page) {
                return null;
            }

            @Override
            public Optional<Task> findById(Integer id) {
                return Optional.ofNullable(tasks.get(id));
            }

            @Override
            public Task save(final Task entity) {

                int key = tasks.size() + 1;
                Field field = null;
                try {
                    field = Task.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity,key);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                   throw new RuntimeException(e);
                }
                tasks.put(key,entity);
                return  tasks.get(key);
            }

            @Override
            public List<Task> findByDone(boolean done) {
                return null;
            }

            @Override
            public boolean existsById(Integer id) {
                return tasks.containsKey(id);
            }

            @Override
            public boolean existsByDoneIsFalseAndGroup_Id(Integer groupId) {
                return false;
            }

            @Override
            public List<Task> findAllByGroup_Id(Integer groupId) {
                return List.of();
            }
        };
    }
}
