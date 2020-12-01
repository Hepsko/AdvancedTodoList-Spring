package com.project.todolist.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
 public class Task  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Task description must be not null and not empty")
    private String description;
    private boolean done;
    private LocalDateTime deadline;
    @Embedded
    private Audit audit = new Audit();
    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;

    Task() {
    }

    public int getId() {
        return id;
    }

     void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

     void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

     void setDeadLine(LocalDateTime deadline) {
        this.deadline = deadline;
    }

     TaskGroup getGroup() {
        return group;
    }

     void setGroup(TaskGroup group) {
        this.group = group;
    }

    public void updateFrom(final Task source)
        {
            description=source.description;
            done=source.done;
            deadline=source.deadline;
            group=source.group;
        }

}
