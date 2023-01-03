package com.abcode.taskproject.controller;

import com.abcode.taskproject.payload.TaskDto;
import com.abcode.taskproject.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // save the task into the database
    @PostMapping("/{userid}/tasks")
    public ResponseEntity<TaskDto> saveTask(
            @PathVariable(name="userid") long userid,
            @RequestBody TaskDto taskDto
    ){
        return new ResponseEntity<>(taskService.saveTask(userid, taskDto), HttpStatus.CREATED);
    }

    // get all task
//    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @GetMapping("/{userid}/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks(
            @PathVariable(name="userid") long userid
            ){
        return new ResponseEntity<>(taskService.getAllTasks(userid), HttpStatus.OK);
    }

    // get single task
    @GetMapping("/{userid}/tasks/{taskid}")
    public ResponseEntity<TaskDto> getTask(
            @PathVariable(name="userid") long userid,
            @PathVariable(name="taskid") long taskid
    ){
        return new ResponseEntity<>(taskService.getTask(userid, taskid), HttpStatus.OK);
    }

    // delete single task
    @DeleteMapping("/{userid}/tasks/{taskid}")
    public ResponseEntity<String> deleteTask(
            @PathVariable(name="userid") long userid,
            @PathVariable(name="taskid") long taskid
    ){
        taskService.deleteTask(userid, taskid);
        return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
    }
}
