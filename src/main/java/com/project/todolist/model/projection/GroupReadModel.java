package com.project.todolist.model.projection;

import com.project.todolist.model.Task;
import com.project.todolist.model.TaskGroup;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupReadModel {
    private String description;
    private LocalDateTime deadline;
    private Set<GroupTaskReadModel> tasks;

    GroupReadModel(TaskGroup sorce)
    {
        this.description=sorce.getDescription();
       sorce.getTasks().stream().map(Task::getDeadline).max(LocalDateTime::compareTo).ifPresent(date -> deadline=date);
       tasks=sorce.getTasks().stream().map(GroupTaskReadModel::new).collect(Collectors.toSet());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Set<GroupTaskReadModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<GroupTaskReadModel> tasks) {
        this.tasks = tasks;
    }
}
