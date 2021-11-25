package com.tw.userservice.web;

import com.tw.userservice.exception.AuthorizationException;
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
        User user = Optional.ofNullable(userRepository.findUserByUserId(header.parsingToken(token)))
                .orElseThrow(()-> new UserNotFoundException("User "+header.parsingToken(token)+"Not Found"));
        return user.getRole().equals("admin");
    }
    public Boolean authorize(String token, String userId){
        if(header.parsingToken(token)==null){
            throw new AuthorizationException("Token is Error");
        }
        User user = Optional.ofNullable(userRepository.findUserByUserId(userId))
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        return header.parsingToken(token).equals(user.getUserId());
    }
    public Boolean authorize(String token,Long id){
        Task task = Optional.ofNullable(taskRepository.findTaskById(id))
                .orElseThrow(()->new TaskNotFoundException("Task Not Found"));
        User user = Optional.ofNullable(userRepository.findUserByUserId(task.getUserId()))
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        if(header.parsingToken(token)==null){
            throw new AuthorizationException("Token is Error");
        }
        return header.parsingToken(token).equals(user.getUserId());
    }
    public Boolean authorize(String token,Long id,String userId){
        Task task = Optional.ofNullable(taskRepository.findTaskById(id))
                .orElseThrow(()->new TaskNotFoundException("Task Not Found"));
        User user = Optional.ofNullable(userRepository.findUserByUserId(task.getUserId()))
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        if(userRepository.findUserByUserId(userId)==null){
            throw new UserNotFoundException("User Not Found");
        }
        if(header.parsingToken(token)==null){
            throw new AuthorizationException("Token is Error");
        }
        return header.parsingToken(token).equals(user.getUserId());
    }
}
