package com.tw.userservice.web;

import com.tw.userservice.exception.AuthorizationException;
import com.tw.userservice.exception.TaskNotFoundException;
import com.tw.userservice.modle.GetTasksLevel;
import com.tw.userservice.modle.Task;
import com.tw.userservice.modle.TaskDto;
import com.tw.userservice.service.TaskService;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    // GET /tasks, /tasks/task_id, /tasks?userId=xxx
    // POST /tasks body
    // PUT /tasks body
    // DELETE /tasks/task_id
    @Autowired
    TaskService taskService;

    @Autowired
    Authorization authorization;


    @PostMapping()
    public String createTasks(@RequestBody TaskDto dto){

       return taskService.createTasks(dto.getUserId(),dto.getTasks());

    }

    @GetMapping()
    public List<Task> getTasksForUser(@RequestParam(value = "userId") String userId){

        return taskService.getTasksForUser(userId);
    }

    @GetMapping("all")
    public List<Task> getAllTasks(@RequestParam(value = "userId") String userId){
        if(authorization.getAuthorization(userId)){
            return taskService.getAllTasks();
        }
        throw new AuthorizationException("USER " +userId+ " NO AUTHORIZATION");
    }


    @GetMapping("level")
    public List<Task> getTaskByStatus(@RequestParam("userId") String userId, @RequestBody GetTasksLevel level){

        return taskService.getTasksByStatus(userId,level);
    }

    @DeleteMapping()
    public String deleteTaskById(@RequestParam("userId") String userId,@RequestBody Long id){

        return taskService.deleteTaskById(userId,id);
    }

}
