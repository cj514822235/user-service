package com.tw.userservice.web;

import com.tw.userservice.modle.ChangeUserInfo;

import com.tw.userservice.modle.User;
import com.tw.userservice.modle.UserDetails;
import com.tw.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/users")

public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String createUser (@RequestBody User user) {
       return userService.createUser(user);
    }

    @GetMapping("/{userId}")
    public UserDetails findByUserId (@PathVariable(value = "userId") String userId){
        return userService.findByUserId(userId);
    }

    @GetMapping()
    public List<UserDetails> findAllUsers(){
        return userService.findAllUsers();
    }

    @PostMapping("/{userId}/updateInfo")
    public void updateUserInfo(@PathVariable(value = "userId")String userId,
                               @RequestBody ChangeUserInfo changeUserInfo){

        userService.updateUserInfo(userId , changeUserInfo);
    }

    @GetMapping("/{userId}/deletion")
    public String deleteUserInfo(@PathVariable(value = "userId")String userId){
       return userService.deleteUserInfo(userId);
    }


    }







