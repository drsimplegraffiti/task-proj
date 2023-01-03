package com.abcode.taskproject.payload;

import lombok.Getter;
import lombok.Setter;

@Getter // @Getter is a Lombok annotation to create all the getters automatically, avoid writing them manually
@Setter // @Setter is a Lombok annotation to create all the setters automatically, avoid writing them manually
public class TaskDto { // TaskDto is a class that represents the data that will be sent to the client
    private long id;
    private String taskname;
}
