package com.project.todolist.model.projection;

import com.project.todolist.model.Task;
import com.project.todolist.model.TaskGroup;

import java.time.LocalDateTime;

public class GroupTaskWriteModel {
    private String description;
    private LocalDateTime deadline;

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

    Task toTask(TaskGroup group)
    {
        return new Task(description,deadline, group);
    }
}
