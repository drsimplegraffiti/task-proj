package com.abcode.taskproject;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TaskprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskprojectApplication.class, args);
	}

	@Bean             // @Bean is a Spring annotation to create a bean i.e an object of the class ModelMapper
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
