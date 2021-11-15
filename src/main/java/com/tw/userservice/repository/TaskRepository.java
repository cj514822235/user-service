package com.tw.userservice.repository;

import com.tw.userservice.modle.Level;
import com.tw.userservice.modle.Task;

import com.tw.userservice.modle.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findTasksByUserId(Long userId);

    List<Task> findTasksByLevel(Level level);

}
