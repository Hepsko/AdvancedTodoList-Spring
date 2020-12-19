package com.project.todolist.logic;

import com.project.todolist.TaskConfigurationProperties;
import com.project.todolist.model.ProjectRepository;
import com.project.todolist.model.TaskGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just one group and undone group exists ")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupExists_throwsIllegalStateException() {
        //given
        var mockGroupRepository =mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProjectId(anyInt())).thenReturn(true);
        //and
        TaskConfigurationProperties mockConfigurationProperties = configReturning(false);
        //system under test
        var toTest=new ProjectService(null, mockGroupRepository, mockConfigurationProperties);

        //when
        var exception= catchThrowable(()-> toTest.createGroup(1,LocalDateTime.now()));
        //then
        assertThat(exception).isInstanceOf(IllegalStateException.class).hasMessageContaining("one undone group");
        }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no projects for a given id")
    void createGroup_configurationOkAnd_noProjects_throwsIIllegalArgumentException() {
        //given
        var mockRepository= mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskConfigurationProperties mockConfigurationProperties = configReturning(true);
        //system under test
        var toTest=new ProjectService(mockRepository, null, mockConfigurationProperties);

        //when
        var exception= catchThrowable(()-> toTest.createGroup(1,LocalDateTime.now()));
        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Project not found");
    }

    private TaskConfigurationProperties configReturning(final boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowallowMultipleTasks()).thenReturn(result);
        var mockConfigurationProperties = mock(TaskConfigurationProperties.class);
        when(mockConfigurationProperties.getTemplate()).thenReturn(mockTemplate);
        return mockConfigurationProperties;
    }
}