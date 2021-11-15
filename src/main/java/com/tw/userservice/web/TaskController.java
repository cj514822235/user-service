package com.tw.userservice.web;

import com.tw.userservice.modle.GetLevelTasks;
import com.tw.userservice.modle.Level;
import com.tw.userservice.modle.Task;
import com.tw.userservice.modle.User;
import com.tw.userservice.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController (TaskService taskService){ this.taskService = taskService;}

    @PostMapping("{userId}/created")
    public String createTasks(@PathVariable("userId") String userId, @RequestBody List<Task> tasks){

       return taskService.createTasks(userId,tasks);
    }

    @GetMapping("{userId}/")
    public List<Task> getTasksForUser(@PathVariable("userId") String userId){

        return taskService.getTasksForUser(userId);
    }

    @GetMapping("{userId}/status")
    public List<Task> getTaskByStatus(@PathVariable("userId") String userId, @RequestBody GetLevelTasks level){
        return taskService.getTasksByStatus(userId,level);
    }

    @GetMapping("{userId}/deletion")
    public String deleteTaskById(@PathVariable("userId") String userId,@RequestBody Long id){

        return taskService.deleteTaskById(userId,id);
    }



}
