package com.project.todolist.logic;


import com.project.todolist.TaskConfigurationProperties;
import com.project.todolist.model.TaskGroup;
import com.project.todolist.model.TaskGroupRepository;
import com.project.todolist.model.TaskRepository;
import com.project.todolist.model.projection.GroupReadModel;
import com.project.todolist.model.projection.GroupWriteModel;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class TaksGroupService {
   private TaskGroupRepository repsitory;
    private TaskRepository taskRepository;
TaksGroupService(final TaskGroupRepository repsitory, final TaskRepository taskRepository)
    {
        this.repsitory=repsitory;
        this.taskRepository=taskRepository;
    }

public GroupReadModel createGroup(GroupWriteModel source)
    {
        TaskGroup result= repsitory.save(source.toGroup());
        return new GroupReadModel(result);
    }

List<GroupReadModel> readAll()
    {
        return repsitory.findAll().stream().map(GroupReadModel::new).collect(Collectors.toList());
    }

public void toggleGroup(int groupId) {
    if(taskRepository.existsByDoneIsFalseAndGroup_Id(groupId))
        {
            throw new IllegalStateException("Group has unclosed task. You must done all the tasks first!");
        }
   TaskGroup result = repsitory.findById(groupId)
            .orElseThrow(()-> new IllegalArgumentException("TaskGroup not found"));
    result.setDone(!result.isDone());
    repsitory.save(result);
}
}
