package com.tw.userservice.web;

import com.tw.userservice.exception.AuthorizationException;
import com.tw.userservice.modle.GetTasksLevel;
import com.tw.userservice.modle.Task;
import com.tw.userservice.modle.TaskDto;
import com.tw.userservice.service.Header;
import com.tw.userservice.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @Autowired
    Header header;


    @PostMapping()
    public String createTasks(@RequestBody TaskDto dto){

       return taskService.createTasks(dto.getUserId(),dto.getTasks());

    }

    @GetMapping()
    public List<Task> getTasksForUser(@RequestParam(value = "userId") String userId){


        return taskService.getTasksForUser(userId);
    }

    @GetMapping("all-task")
    public List<Task> getAllTasks(@RequestHeader(value = "token") String token){
        if(authorization.getAuthorization(header.parsingToken(token))){
            return taskService.getAllTasks();
        }
        throw new AuthorizationException("USER " + header.parsingToken(token)+ " NO AUTHORIZATION");
    }


    @GetMapping("task-level")
    public List<Task> getTaskByStatus(@RequestParam(value = "userId") String userId,@RequestBody GetTasksLevel level){

        return taskService.getTasksByStatus(userId,level);
    }

    @DeleteMapping("{id}")
    public String deleteTaskById(@RequestHeader(value = "token") String token,@PathVariable(value = "id")Long id){

        return taskService.deleteTaskById(header.parsingToken(token),id);
    }

    @DeleteMapping()
    public String deleteAnyTask(@RequestHeader(value = "token") String token,@RequestBody Long id){
        if(authorization.getAuthorization(header.parsingToken(token))){
            return taskService.deleteAnyTask(id);
        }
        throw new AuthorizationException("USER " + header.parsingToken(token)+ " NO AUTHORIZATION");
    }



}
