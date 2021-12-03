package com.tw.userservice.web;

import com.tw.userservice.exception.AuthorizationException;

import com.tw.userservice.model.ModifyTaskInfo;
import com.tw.userservice.model.ShareTask;
import com.tw.userservice.model.Task;
import com.tw.userservice.model.TaskDto;
import com.tw.userservice.service.TaskService;
import com.tw.userservice.utils.Authorization;
import com.tw.userservice.utils.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public String createTasks(@RequestHeader(value = "token") String token,@RequestBody TaskDto dto){
        if(authorization.authorizeIsAdmin(token)||authorization.authorize(token,dto.getUserId())){
            return taskService.createTasks(dto.getUserId(),dto.getTasks());
        }
        throw new AuthorizationException("No Authorization");
    }


    @GetMapping()
    public List<Task> getTask(@RequestHeader(value = "token") String token,
                              @RequestParam(value = "userId",required = false) String userId,
                              @RequestParam(value = "level",required = false) String level){

        if(authorization.authorizeIsAdmin(token)||authorization.authorize(token,userId)){
            return taskService.getTasks(userId,level);
        }
        throw new AuthorizationException("No Authorization");
    }

    @GetMapping("/{id}")
    public Task getTaskById(@RequestHeader(value = "token")String token, @PathVariable(value = "id") Long id ){

        if(authorization.authorizeIsAdmin(token)||authorization.authorize(token,id)){
            return taskService.getTask(id);
        }
        throw new AuthorizationException("No Authorization");
    }

    @DeleteMapping("/{id}")
    public Long deleteAnyTask(@RequestHeader(value = "token") String token,@PathVariable(value = "id")Long id){

        if(authorization.authorizeIsAdmin(token)||authorization.authorize(token,id)){
            return taskService.deleteTask(id);
        }
        throw new AuthorizationException("USER " + header.parsingToken(token)+ " NO AUTHORIZATION");
    }

    @PostMapping("/sharing")
    public Long shareTask(@RequestHeader(value = "token") String token, @RequestBody ShareTask shareTask){
        if(authorization.authorizeIsAdmin(token)||authorization.authorize(token,shareTask.getTaskId(),shareTask.getUserId())){
            return taskService.shareTask(shareTask);

        }
        throw new AuthorizationException("No Authorization");
    }
    @PutMapping("/{id}")
    public Long modifyTask(@RequestHeader(value = "token") String token , @PathVariable(value = "id") Long id,
                             @RequestBody ModifyTaskInfo modifyTaskInfo){
        if(authorization.authorizeIsAdmin(token)||authorization.authorize(token,id)){
            return taskService.modifyTask(id,modifyTaskInfo);
        }
        throw new AuthorizationException("No Authorization");
    }


}
