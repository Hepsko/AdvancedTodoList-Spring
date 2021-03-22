package com.project.todolist.logic;


import com.project.todolist.TaskConfigurationProperties;
import com.project.todolist.model.Project;
import com.project.todolist.model.TaskGroup;
import com.project.todolist.model.TaskGroupRepository;
import com.project.todolist.model.TaskRepository;
import com.project.todolist.model.projection.GroupReadModel;
import com.project.todolist.model.projection.GroupWriteModel;


import java.util.List;
import java.util.stream.Collectors;


public class TaksGroupService {
   private TaskGroupRepository repository;
    private TaskRepository taskRepository;
TaksGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository)
    {
        this.repository=repository;
        this.taskRepository=taskRepository;
    }

public GroupReadModel createGroup(GroupWriteModel source)
    {
        return createGroup(source,null);
    }

    GroupReadModel createGroup(final GroupWriteModel  source, final Project project) {
        TaskGroup result= repository.save(source.toGroup(project));
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll()
    {
        return repository.findAll().stream().map(GroupReadModel::new).collect(Collectors.toList());
    }

public void toggleGroup(int groupId) {
    if(taskRepository.existsByDoneIsFalseAndGroup_Id(groupId))
        {
            throw new IllegalStateException("Group has unclosed task. You must done all the tasks first!");
        }
   TaskGroup result = repository.findById(groupId)
            .orElseThrow(()-> new IllegalArgumentException("TaskGroup not found"));
    result.setDone(!result.isDone());
    repository.save(result);
}


}
