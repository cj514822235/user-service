package com.tw.userservice.service;

import com.tw.userservice.modle.ChangeUserInfo;
import com.tw.userservice.modle.Task;
import com.tw.userservice.modle.User;
import com.tw.userservice.modle.UserDetails;
import com.tw.userservice.repository.TaskRepository;
import com.tw.userservice.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Service
public class UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public UserService(UserRepository userRepository, TaskRepository taskRepository) {

        this.userRepository = userRepository;
        this.taskRepository = taskRepository;

    }


    public String createUser(User user) {
        String userId = orderIdGenerator();
        user.setUserId(userId);
        user.setStatus(true);
        userRepository.save(user);
        return userId;
    }


    public UserDetails findByUserId(String userId){
        User user =  userRepository.findUserByUserId(userId);
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
            return null;
    }



    private String orderIdGenerator() {
        return String.format("%09d", (new Date().getTime()) % 1000000000)+ RandomStringUtils.randomNumeric(4);
    }


    public List<UserDetails> findAllUsers() {
        List<User> userList = userRepository.findAll();
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
        User user = userRepository.findUserByUserId(userId);
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
    }

    public String deleteUserInfo(String userId) {
        User user = userRepository.findUserByUserId(userId);
        if(user.getStatus()) {
            user.setStatus(false);

           List<Task> tasks = taskRepository.findTasksByUserId(user.getId());
           tasks.forEach(task -> task.setStatus(false));
           taskRepository.saveAll(tasks);

           userRepository.save(user);
            return "successfully delete";
        }
        return "Not found";
    }

}
