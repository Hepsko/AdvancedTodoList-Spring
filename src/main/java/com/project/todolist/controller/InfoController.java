package com.project.todolist.controller;


import com.project.todolist.TaskConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class InfoController {
    private TaskConfigurationProperties myProp;
    private DataSourceProperties dataSource;

    public InfoController(final TaskConfigurationProperties myProp, final DataSourceProperties dataSource) {
        this.myProp = myProp;
        this.dataSource = dataSource;
    }

    @GetMapping("/url")
    String url(){
        return dataSource.getUrl();
    }
    @GetMapping("/prop")
    boolean myProp()
    {
        return myProp.getTemplate().isAllowMultipleTasks();

    }
}
