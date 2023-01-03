package com.abcode.taskproject.serviceimpl;

import com.abcode.taskproject.entity.Task;
import com.abcode.taskproject.entity.Users;
import com.abcode.taskproject.exception.APIException;
import com.abcode.taskproject.exception.TaskNotFound;
import com.abcode.taskproject.exception.UserNotFound;
import com.abcode.taskproject.payload.TaskDto;
import com.abcode.taskproject.repository.TaskRepository;
import com.abcode.taskproject.repository.UserRepository;
import com.abcode.taskproject.service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public TaskDto saveTask(long userid, TaskDto taskDto) {
        // check if user exists in db first
        Users user = userRepository.findById(userid).orElseThrow(
                () ->new UserNotFound(String.format("User with id %d not found", userid))
        );

        // Convert the taskDto to task entity first using the model mapper
        Task task = modelMapper.map(taskDto, Task.class);
        // Set the user id for the task
        task.setUsers(user);
        Task savedTask =  taskRepository.save(task);

        // convert the Entity back to DTo to be shown to the client
        return modelMapper.map(savedTask, TaskDto.class);
    }

    @Override
    public List<TaskDto> getAllTasks(long userid) {
        // check if user exists in db first
        userRepository.findById(userid).orElseThrow(
                () ->new UserNotFound(String.format("User with id %d not found", userid))
        );

        // find all tasks attached to a user
        List<Task> tasks = taskRepository.findAllByUsersId(userid);

        return tasks.stream().map(task -> modelMapper.map(task, TaskDto.class)).collect(Collectors.toList());
    }


    @Override
    public TaskDto getTask(long userid, long taskid) {
        // check if user exists in db first
        Users users =  userRepository.findById(userid).orElseThrow(
                () ->new UserNotFound(String.format("User with id %d not found", userid))
        );

        // find task attached to a user by id and throw exception if not found
        Task task = taskRepository.findById(taskid).orElseThrow(
                () ->new TaskNotFound(String.format("Task with id %d not found", taskid))
        );

        // check if the task belongs to the user
        if(users.getId() != task.getUsers().getId()){
            throw new APIException(String.format("Task with id %d does not belong to user with id %d", taskid, userid));
        }
        return modelMapper.map(task, TaskDto.class);
    }

    @Override
    public void deleteTask(long userid, long taskid) {
        // check if user exists in db first
        Users users =  userRepository.findById(userid).orElseThrow(
                () ->new UserNotFound(String.format("User with id %d not found", userid))
        );

        // find task attached to a user by id and throw exception if not found
        Task task = taskRepository.findById(taskid).orElseThrow(
                () ->new TaskNotFound(String.format("Task with id %d not found", taskid))
        );

        // check if the task belongs to the user
        if(users.getId() != task.getUsers().getId()){
            throw new APIException(String.format("Task with id %d does not belong to user with id %d", taskid, userid));
        }

        taskRepository.deleteById(taskid); // this helps to delete the task from the db using the task id
    }


}
