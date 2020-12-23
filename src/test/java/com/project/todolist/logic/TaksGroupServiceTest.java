package com.project.todolist.logic;

import com.project.todolist.model.TaskGroup;
import com.project.todolist.model.TaskGroupRepository;
import com.project.todolist.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaksGroupServiceTest {

    @Test
    @DisplayName("Should throw IllegalAccessException with done all the tasks first")
    void toggle_group_with_unclosed_task_throws_IllegalStateException() {
        TaskRepository MockTaskRepository = TaskRepositoryReturning(true);
        var toTest =new TaksGroupService(null, MockTaskRepository);
        var result= catchThrowable(()->toTest.toggleGroup(2));
        assertThat(result).isInstanceOf(IllegalStateException.class).
                hasMessageContaining("Group has unclosed task. You must done all the tasks first!");
    }




    @Test
    @DisplayName("Should throw IllegalArgumentException with done all the tasks first")
    void toggle_group_with_closed_task_and_there_is_no_task_group_throws_IllegalArgumentException() {
        TaskRepository MockTaskRepository = TaskRepositoryReturning(false);
        var MockTaskGroupRepository= mock(TaskGroupRepository.class);
        when(MockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.empty());
        var toTest =new TaksGroupService(MockTaskGroupRepository , MockTaskRepository);
        var result= catchThrowable(()->toTest.toggleGroup(2));
        assertThat(result).isInstanceOf(IllegalArgumentException.class).
                hasMessageContaining("TaskGroup not found");

    }

    @Test
    @DisplayName("Should  toggle group")
    void toggle_group_with_closed_task_and__task_group()  {
       TaskRepository MockTaskRepository = TaskRepositoryReturning(false);

        var group = new TaskGroup();
        var beforeToggle = group.isDone();

        var MockTaskGroupRepository= mock(TaskGroupRepository.class);
        when(MockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.of(group));

        var toTest=new TaksGroupService(MockTaskGroupRepository,MockTaskRepository);
        toTest.toggleGroup(0);

        assertThat(group.isDone()).isEqualTo(!beforeToggle);

    }

    private TaskRepository TaskRepositoryReturning(boolean b) {
        var MockTaskRepository = mock(TaskRepository.class);
        when(MockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(b);
        return MockTaskRepository;
    }


}