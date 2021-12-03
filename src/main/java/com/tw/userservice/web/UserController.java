package com.tw.userservice.web;

import com.tw.userservice.exception.AuthorizationException;

import com.tw.userservice.model.ChangeUserInfo;

import com.tw.userservice.model.User;
import com.tw.userservice.model.UserDetails;
import com.tw.userservice.model.UserInfo;
import com.tw.userservice.service.UserService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    Authorization authorization;

    @Autowired
    Header header;


    @GetMapping("token")
    public String getToken(@RequestBody UserInfo userInfo)  {
        return header.getToken(userInfo.getUserId());
    }



    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String createUser(@RequestBody User user) {
        return userService.createUser(user);
    }


    @GetMapping("/{userId}")
    public UserDetails findByUserId(@RequestHeader(value = "token") String token, @PathVariable(value = "userId") String userId) {
        if(authorization.authorize(token,userId)||authorization.authorizeIsAdmin(token)) {
            return userService.findByUserId(userId);
        }
        throw new AuthorizationException("Token Error");
    }

    @GetMapping()
    public List<UserDetails> findAllUsers(@RequestHeader(value = "token") String token) {
        if (authorization.authorizeIsAdmin(token)) {
            return userService.findAllUsers();
        }
        throw new AuthorizationException("USER " + header.parsingToken(token) + " NO AUTHORIZATION");
    }


    @PutMapping("/{userId}")
    public String updateUserInfo(@RequestHeader(value = "token")String token ,@PathVariable(value = "userId") String userId,
                               @RequestBody ChangeUserInfo changeUserInfo) {
        if (authorization.authorize(token,userId)) {
            return  userService.updateUserInfo(userId, changeUserInfo);
        }
        throw new AuthorizationException("Token Error");
    }

    @DeleteMapping("/{userId}")
    public String deleteUserInfo(@RequestHeader(value = "token")String token , @PathVariable(value = "userId") String userId) {

        if (authorization.authorize(token,userId)||authorization.authorizeIsAdmin(token)) {
            return userService.deleteUserInfo(userId);
        }
        throw new AuthorizationException("Token Error");
    }
}









