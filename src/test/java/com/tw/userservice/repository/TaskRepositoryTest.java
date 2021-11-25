package com.tw.userservice.repository;


import com.tw.userservice.model.Level;
import com.tw.userservice.model.Request;
import com.tw.userservice.model.Task;
import com.tw.userservice.model.User;
import com.tw.userservice.service.TaskSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Rollback
@RunWith(SpringRunner.class)
public class TaskRepositoryTest {

    private Task firstTask;

    private Task secondTask;

    private User user;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setup(){
        entityManager.clear();

        user =User.builder()
                .id(1L)
                .name("小明")
                .userId("1234567891011")
                .cellphone("15229928145")
                .role("user")
                .status(true)
                .email("12345@qq.com")
                .age(10)
                .address("陕西省西安市")
                .build();

        firstTask =Task.builder()
                .id(null)
                .userId(user.getUserId())
                .description("add task1")
                .level(Level.EASY)
                .status(true)
                .build();

        secondTask =Task.builder()
                .id(null)
                .userId(user.getUserId())
                .description("add task2")
                .level(Level.HARD)
                .status(true)
                .build();

        entityManager.persistAndFlush(firstTask);
        entityManager.persistAndFlush(secondTask);

    }

    @Test
    public void should_return_task_when_task_id_exists(){
        Task task = taskRepository.findTaskById(1L);

        Assertions.assertEquals(firstTask,task);

    }
    @Test
    public void should_return_tasks_when_user_id_exists() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(firstTask);
        tasks.add(secondTask);

        List<Task> result = taskRepository.findTasksByUserId(user.getUserId());

        Assertions.assertEquals(tasks, result);
    }


    @Test
    public void should_return_tasks_bu_different_conditions(){
        Request request = new Request("1234567891011",Level.HARD);
        TaskSpecification taskSpecification = new TaskSpecification(request);

        List<Task> result = taskRepository.findAll(taskSpecification);

        Assertions.assertEquals(result.get(0), secondTask);
    }



}
