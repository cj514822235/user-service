package com.tw.userservice.service;


import com.tw.userservice.exception.TaskNotFoundException;
import com.tw.userservice.exception.UserNotFoundException;
import com.tw.userservice.modle.GetTasksLevel;
import com.tw.userservice.modle.Task;
import com.tw.userservice.modle.User;
import com.tw.userservice.repository.TaskRepository;
import com.tw.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;


    public TaskService(UserRepository userRepository ,TaskRepository taskRepository){
        this.userRepository =  userRepository;
        this.taskRepository = taskRepository;
    }

    public String createTasks(String userId, List<Task> tasks) {

        User user = Optional.ofNullable(userRepository.findUserByUserId(userId))
                .orElseThrow(()->new UserNotFoundException("User Not Found"));

        tasks.forEach(task -> {
            task.setStatus(true);
            task.setUserId(user.getId());
        });
        taskRepository.saveAll(tasks);
        return userId;
    }


    public List<Task> getTasksForUser(String  userId){
        User user = Optional.ofNullable(userRepository.findUserByUserId(userId))
                .orElseThrow(()->new UserNotFoundException("User Not Found"));

        if(user.getStatus()) {
            if(taskRepository.findTasksByUserId(user.getId()).isEmpty()){
                throw new TaskNotFoundException("Task Not Found");
            }
            return taskRepository.findTasksByUserId(user.getId()).stream()
                    .filter(task -> task.getStatus().equals(true)).collect(Collectors.toList());
        }
        throw new UserNotFoundException("User Not Found");
    }


    public List<Task> getTasksByStatus(String userId, GetTasksLevel level) {

        User user = Optional.ofNullable(userRepository.findUserByUserId(userId))
                .orElseThrow(()->new UserNotFoundException("User Not Found"));

        if (user.getStatus()){
            List<Task> tasks = taskRepository.findTasksByUserId(user.getId());
            if(tasks.isEmpty()){
                throw new TaskNotFoundException("Task Not Found");
            }
            return tasks.stream().filter(task -> task.getLevel().equals(level.getLevel())).collect(Collectors.toList());
        }
        throw new UserNotFoundException("User Not found");
    }


    public String deleteTaskById(String userId, Long id) {
        User user = Optional.ofNullable(userRepository.findUserByUserId(userId))
                .orElseThrow(()->new UserNotFoundException("User Not Found"));

        if(user.getStatus()) {
            List<Task> tasks = taskRepository.findTasksByUserId(user.getId());
            if(tasks.isEmpty()){
                throw new TaskNotFoundException("Task Not Found");
            }
            for (Task task : tasks) {
                if (task.getId().equals(id) && task.getStatus().equals(true)) {
                    task.setStatus(false);
                    taskRepository.save(task);
                    return "successfully delete";
                }
            }
            throw new TaskNotFoundException("Task Not Found");
        }
        throw new UserNotFoundException("User Not found");
    }
}
