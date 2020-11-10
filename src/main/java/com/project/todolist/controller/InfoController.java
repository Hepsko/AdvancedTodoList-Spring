package com.project.todolist.controller;


import com.project.todolist.TaskConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {
    private TaskConfigurationProperties myProp;
    private DataSourceProperties dataSource;

    public InfoController(final TaskConfigurationProperties myProp, final DataSourceProperties dataSource) {
        this.myProp = myProp;
        this.dataSource = dataSource;
    }

    @GetMapping("/info/url")
    String url(){
        return dataSource.getUrl();
    }
    @GetMapping("/info/prop")
    boolean myProp()
    {
        return myProp.getTemplate().isAllowallowMultipleTasks();

    }
}
