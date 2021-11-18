package com.tw.userservice.service;

import com.tw.userservice.exception.UserNotFoundException;
import com.tw.userservice.modle.ChangeUserInfo;
import com.tw.userservice.modle.Task;
import com.tw.userservice.modle.User;
import com.tw.userservice.modle.UserDetails;
import com.tw.userservice.repository.TaskRepository;
import com.tw.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Access;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class UserService {

    public static final String USER_NOT_FOUND = "User Not found";

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  TaskRepository taskRepository;

//    public UserService(UserRepository userRepository, TaskRepository taskRepository) {
//
//        this.userRepository = userRepository;
//        this.taskRepository = taskRepository;
//
//    }


    public String createUser(User user) {
        String userId = orderIdGenerator();
        user.setUserId(userId);
        user.setStatus(true);
        userRepository.save(user);
        return userId;
    }


    public UserDetails findByUserId(String userId){
        User user =  Optional.ofNullable(userRepository.findUserByUserId(userId))
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
       // log.error("User Not Found " + userId);
        if(user.getStatus()) {
            return UserDetails.builder()
                    .userId(user.getUserId())
                    .age(user.getAge())
                    .address(user.getAddress())
                    .cellphone(user.getCellphone())
                    .email(user.getEmail())
                    .name(user.getName())
                    .build();
        }
         log.error("User"+userId+"Not Found");
         throw new UserNotFoundException(USER_NOT_FOUND);

    }



    private String orderIdGenerator() {
        return String.format("%09d", (new Date().getTime()) % 1000000000)+ RandomStringUtils.randomNumeric(4);
    }


    public List<UserDetails> findAllUsers() {
        List<User> userList = Optional.ofNullable(userRepository.findAll())
                .orElseThrow(()->new UserNotFoundException("Empty"));
        log.error("User Not Found");
        List<UserDetails> userDetails = new ArrayList<>();

        for (User user : userList) {
            userDetails.add(UserDetails.builder()
                    .userId(user.getUserId())
                    .age(user.getAge())
                    .address(user.getAddress())
                    .cellphone(user.getCellphone())
                    .email(user.getEmail())
                    .name(user.getName())
                    .build());
        }
        return userDetails;
    }


    public void updateUserInfo(String userId, ChangeUserInfo changeUserInfo) {
        User user = Optional.ofNullable(userRepository.findUserByUserId(userId))
                .orElseThrow(()->new UserNotFoundException("User"+userId+" Not Found"));
        log.error("User "+userId+"Not Found");
        //todo add userInfo,add log
        if(user.getStatus()) {
            if (changeUserInfo.getAge() != null) {
                user.setAge(changeUserInfo.getAge());
            }
            if (changeUserInfo.getAddress() != null) {
                user.setAddress(changeUserInfo.getAddress());
            }
            if (changeUserInfo.getCellphone() != null) {
                user.setCellphone(changeUserInfo.getCellphone());
            }
            if (changeUserInfo.getEmail() != null) {
                user.setEmail(changeUserInfo.getEmail());
            }
            userRepository.save(user);
        }
        log.error("User"+userId+" Not Found");
        throw new UserNotFoundException(USER_NOT_FOUND);
    }

    public String deleteUserInfo(String userId) {
        User user = Optional.ofNullable(userRepository.findUserByUserId(userId))
                .orElseThrow(()->new UserNotFoundException(USER_NOT_FOUND));
        if(user.getStatus()) {
            user.setStatus(false);

           List<Task> tasks = taskRepository.findTasksByUserId(user.getId());
           tasks.forEach(task -> task.setStatus(false));
           taskRepository.saveAll(tasks);

           userRepository.save(user);
            return "successfully delete";
        }
        throw new UserNotFoundException(USER_NOT_FOUND);
    }

}
