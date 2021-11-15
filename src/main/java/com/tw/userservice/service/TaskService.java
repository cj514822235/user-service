package com.tw.userservice.service;


import com.tw.userservice.modle.GetLevelTasks;
import com.tw.userservice.modle.Task;
import com.tw.userservice.modle.User;
import com.tw.userservice.repository.TaskRepository;
import com.tw.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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

        User user = userRepository.findUserByUserId(userId);
        if(user.getStatus()) {
            tasks.forEach(task -> {
                task.setStatus(true);
                task.setUser(user);
            });
            taskRepository.saveAll(tasks);
            return userId;
        }
        return "user not found";
    }


    public List<Task> getTasksForUser(String  userId){
        User user = userRepository.findUserByUserId(userId);
        return taskRepository.findTasksByUserId(user.getId());
    }


    public List<Task> getTasksByStatus(String userId, GetLevelTasks level) {

        User user = userRepository.findUserByUserId(userId);
        List<Task> tasks = taskRepository.findTasksByUserId(user.getId());
        return tasks.stream().filter(task -> task.getLevel().equals(level.getLevel())).collect(Collectors.toList());
    }


    public String deleteTaskById(String userId, Long id) {
        User user = userRepository.findUserByUserId(userId);
        if(user.getStatus()) {
            List<Task> tasks = taskRepository.findTasksByUserId(user.getId());
            for (Task task : tasks) {
                if (task.getId().equals(id) && task.getStatus().equals(true)) {
                    task.setStatus(false);
                    taskRepository.save(task);
                    return "successfully delete";
                }
            }
            return "task not found";
        }
        return "user not found";
    }
}
