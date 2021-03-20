package com.project.todolist.adapter;

import com.project.todolist.model.Task;
import com.project.todolist.model.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
  interface SqlTaskRepository extends TaskRepository,JpaRepository<Task, Integer> {


  @Override
  boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

  @Override
  List<Task> findAllByGroup_Id(Integer groupId);


}
