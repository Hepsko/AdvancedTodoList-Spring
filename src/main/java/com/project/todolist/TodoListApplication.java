package com.project.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;


@SpringBootApplication
public class TodoListApplication  {

	public static void main(String[] args) {
		SpringApplication.run(TodoListApplication.class, args);


	}

	@Bean
	Validator validator() {
		return new LocalValidatorFactoryBean();
	}


	}



