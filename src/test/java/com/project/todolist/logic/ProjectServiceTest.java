package com.project.todolist.logic;

import com.project.todolist.TaskConfigurationProperties;
import com.project.todolist.model.*;
import com.project.todolist.model.projection.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        var mockGroupRepository =groupRepositoryReturning(true);
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

    @Test
    @DisplayName("should throw IllegalArgumentException  when configured to allow just one group and no group and no projects  for a given id")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupExists_noProjects_throwsIIllegalArgumentException() {
        //given
        var mockRepository= mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        var mockGroupRepository= groupRepositoryReturning(false);
        //and
        TaskConfigurationProperties mockConfigurationProperties = configReturning(true);
        //system under test
        var toTest=new ProjectService(mockRepository, null, mockConfigurationProperties);

        //when
        var exception= catchThrowable(()-> toTest.createGroup(1,LocalDateTime.now()));
        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Project not found");
    }

    @Test
    @DisplayName("should create a new group from project")
    void createGroup_configurationOK_existingProject_createsAndSavesGroup()
    {
        var project= projectWith("bar", Set.of(-1,-2));
        var today= LocalDate.now().atStartOfDay();
        var mockRepository= mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional
                .of(project));
        //and
        inMemoryTaskGroupRepository inMemoryGroupRepo= inMemoryTaskGroupRepository();
        int countBeforeCall = inMemoryGroupRepo.count();
        //and
        TaskConfigurationProperties mockConfig= configReturning(true);
        //system under test
        var toTest = new ProjectService(mockRepository, inMemoryGroupRepo, mockConfig);

        //when
        GroupReadModel result = toTest.createGroup(1, today);

        //then
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task->task.getDescription().equals("foo"));
        assertThat(countBeforeCall+1).isEqualTo(inMemoryGroupRepo.count());
    }

    private Project projectWith(String projectDescription,Set<Integer> daysToDeadline)
    {
        Set<ProjectStep> steps =daysToDeadline.stream().map(days->
        {
            var step = mock(ProjectStep.class);
            when(step.getDescription()).thenReturn("foo");
            when(step.getDaysToDeadline()).thenReturn(days);
            return step;
        }).collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);

        when(result.getSteps()).thenReturn(steps);


        return result;
    }

    private TaskGroupRepository groupRepositoryReturning( boolean result) {
        var mockGroupRepository =mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProjectId(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }

    private TaskConfigurationProperties configReturning(final boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowallowMultipleTasks()).thenReturn(result);
        var mockConfigurationProperties = mock(TaskConfigurationProperties.class);
        when(mockConfigurationProperties.getTemplate()).thenReturn(mockTemplate);
        return mockConfigurationProperties;
    }

    private inMemoryTaskGroupRepository inMemoryTaskGroupRepository()
    {
        return new inMemoryTaskGroupRepository() ;
    }

    private static class inMemoryTaskGroupRepository implements TaskGroupRepository

        {
            private int index=0;
            private Map<Integer,TaskGroup> map = new HashMap<>();
            public int  count(){return map.values().size(); }
            @Override
            public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

            @Override
            public Optional<TaskGroup> findById(Integer i) {
            return Optional.ofNullable(map.get(i));
        }

            @Override
            public TaskGroup save(TaskGroup entity) {
            if(entity.getId()==0) {
                try {
                  var field=  TaskGroup.class.getDeclaredField("id");
                  field.setAccessible(true);
                    field .set(entity,++index);
                }catch (NoSuchFieldException | IllegalAccessException e){
                    throw new RuntimeException(e); }
            }
            map.put(index,entity);
            return entity;
        }

            @Override
            public boolean existsByDoneIsFalseAndProjectId(Integer ProjectId) {
            return map.values().stream().filter(group-> !group.isDone()).allMatch(group -> group.getProject() !=null
                    && group.getProject().getId()==ProjectId);
        }
        }

}