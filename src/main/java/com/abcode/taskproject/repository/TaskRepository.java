package com.abcode.taskproject.repository;

import com.abcode.taskproject.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUsersId(long userid);
}
