#### Create database
```bash
$ mysql -u root -p
```

```sql
create database `taskdb`;
```

#### Use database
```sql
use `taskdb`;
```

---

#### Configure database
```properties

spring.datasource.url=jdbc:mysql://localhost:3306/taskdb
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=Bassguitar1
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
```


---

#### To avoid conversion of Dto to Entity we can use ModelMapper
```xml
<dependency>
    <groupId>org.modelmapper</groupId>
    <artifactId>modelmapper</artifactId>
    <version>3.1.0</version>
</dependency>
```

To use go to your Main.java class and use the following code:
```java
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
```

---

We can avoid codes like:
```java
package com.abcode.taskproject.serviceimpl;

import com.abcode.taskproject.entity.Users;
import com.abcode.taskproject.payload.UserDto;
import com.abcode.taskproject.repository.UserRepository;
import com.abcode.taskproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        // because we cannot parse in the userDto to the repository
        // we need to perform a type conversion of the userDto to the Users entity
        Users user = userDtoToEntity(userDto); // here we are converting the userDto to the Users entity
        Users savedUser  = userRepository.save(user);
        return entityToUserDto(savedUser); // here we are converting the Users entity to the userDto and returning it to the controller layer
    }


// convert the userDto to the Users entity in order to save it in the database
    private Users userDtoToEntity(UserDto userDto){
        Users users = new Users();
        users.setName(userDto.getName());
        users.setEmail(userDto.getEmail());
        users.setPassword(userDto.getPassword());
        return users;
    }

    // convert the Users entity to the userDto in order to return it to the user
    private UserDto entityToUserDto(Users savedUser){
        UserDto userDto = new UserDto();
        userDto.setId(savedUser.getId());
        userDto.setName(savedUser.getName());
        userDto.setEmail(savedUser.getEmail());
        userDto.setPassword(savedUser.getPassword());
        return userDto;
    }
}
```

Using the model mapper you have something like:
```java
package com.abcode.taskproject.serviceimpl;

import com.abcode.taskproject.entity.Task;
import com.abcode.taskproject.entity.Users;
import com.abcode.taskproject.payload.TaskDto;
import com.abcode.taskproject.repository.TaskRepository;
import com.abcode.taskproject.repository.UserRepository;
import com.abcode.taskproject.service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        userRepository.findById(userid).get();

        // Convert the taskDto to task entity first using the model mapper
        Task task = modelMapper.map(taskDto, Task.class);
        Task savedTask =  taskRepository.save(task);

        // convert the Entity back to DTo to be shown to the client
        return modelMapper.map(savedTask, TaskDto.class);
    }

    @Override
    public List<TaskDto> getAllTasks(long userid) {
        return null;
    }
}

```

---

#### Create a new user
> POST http://localhost:8080/api/v1/user

```json
{
    "name": "Abdul",
    "email": "ab@c.com",
    "password":"123456"
}
```

#### Create a new task using the user id
> POST http://localhost:8080/api/{userid}/tasks

```json
{
    "taskname": "Task 1"
}
```

#### Get all tasks using the user id
> GET http://localhost:8080/api/{userid}/tasks

```json
[
    {
        "id": 1,
        "taskname": "Task 1"
    }
]
```

#### Get a task using the user id and task id
Bearer token required
> GET http://localhost:8080/api/{userid}/tasks/{taskid}

```json
{
    "id": 1,
    "taskname": "Task 1"
}
```

#### Delete a task using the user id and task id
Bearer token required
> DELETE http://localhost:8080/api/{userid}/tasks/{taskid}

```json
{
    "id": 1,
    "taskname": "Task 1"
}
```

---

##### Spring boot security
Add the following dependency to your pom.xml file:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

If you run the application now, you will see that the application is secured and you will be prompted to enter a username and password.
With form base auth your credentials are:
```text
username: user
password: generated password in the console
```