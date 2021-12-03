package com.tw.userservice.repository;


import com.tw.userservice.model.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, String>, JpaSpecificationExecutor<Task> {
    List<Task> findTasksByUserId(String userId);
    Task findTaskById(Long id);

}
