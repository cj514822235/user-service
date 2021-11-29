package com.tw.userservice.web;

import com.tw.userservice.exception.AuthorizationException;
import com.tw.userservice.exception.BadRequest;
import com.tw.userservice.exception.TaskNotFoundException;
import com.tw.userservice.exception.UserNotFoundException;
import com.tw.userservice.model.Task;
import com.tw.userservice.model.User;
import com.tw.userservice.repository.TaskRepository;
import com.tw.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.tw.userservice.service.Header;


import java.util.Optional;



@Component
public  class Authorization {
    @Autowired
    private UserRepository  userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private Header header;

    public Boolean authorizeIsAdmin(String token){
        if(token.isEmpty()){
            throw new BadRequest("token error");
        }
        User user = Optional.ofNullable(userRepository.findUserByUserId(header.parsingToken(token)))
                .filter(user1 ->user1.getStatus().equals(true) )
                .orElseThrow(()-> new UserNotFoundException("No  Authorization"));
        return user.getRole().equals("admin");
    }

    public Boolean authorize(String token, String userId){
        if(token.isEmpty()){
            throw new AuthorizationException("Token is Error");
        }
        User user = Optional.ofNullable(userRepository.findUserByUserId(userId)).filter(user1 -> user1.getStatus().equals(true))
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        return header.parsingToken(token).equals(user.getUserId());
    }

    public Boolean authorize(String token,Long taskId){
        if(token.isEmpty()){
            throw new BadRequest("token error");
        }
        Task task = Optional.ofNullable(taskRepository.findTaskById(taskId)).filter(task1 -> task1.getStatus().equals(true))
                .orElseThrow(()->new TaskNotFoundException("Task Not Found"));
        User user = Optional.ofNullable(userRepository.findUserByUserId(task.getUserId()))
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        if(header.parsingToken(token)==null){
            throw new AuthorizationException("Token is Error");
        }
        return header.parsingToken(token).equals(user.getUserId());
    }

    public Boolean authorize(String token,Long taskId,String userId){
        if(token.isEmpty()){
            throw new BadRequest("token error");
        }
        if(taskId==null){
            throw new BadRequest("taskId error");
        }
        Task task = Optional.ofNullable(taskRepository.findTaskById(taskId))
                .orElseThrow(()->new TaskNotFoundException("Task Not Found"));
        User user = Optional.ofNullable(userRepository.findUserByUserId(task.getUserId()))
                .orElseThrow(()->new UserNotFoundException("User Not Found"));

        Optional.ofNullable(userRepository.findUserByUserId(userId))
                .orElseThrow(()->new UserNotFoundException("User Not Found"));

        if(header.parsingToken(token)==null){
            throw new AuthorizationException("Token is Error");
        }
        return header.parsingToken(token).equals(user.getUserId());
    }
}
