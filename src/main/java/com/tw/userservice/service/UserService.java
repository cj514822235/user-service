package com.tw.userservice.service;

import com.tw.userservice.exception.UserNotFoundException;
import com.tw.userservice.exception.UserRepetitionException;
import com.tw.userservice.model.ChangeUserInfo;
import com.tw.userservice.model.Task;
import com.tw.userservice.model.User;
import com.tw.userservice.model.UserDetails;
import com.tw.userservice.repository.TaskRepository;
import com.tw.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
@Transactional
public class UserService {

    public static final String USER_NOT_FOUND = "User Not found";

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  TaskRepository taskRepository;




    public String createUser(User user) {
        if(userRepository.findUserByCellphone(user.getCellphone())==null) {
            String userId = orderIdGenerator();
            user.setUserId(userId);
            user.setStatus(true);
            userRepository.save(user);
            return userId;
        }
        throw new UserRepetitionException("The phone number must be unique");
    }


    public UserDetails findByUserId(String userId){
        User user =  Optional.ofNullable(userRepository.findUserByUserId(userId))
                .orElseThrow(()->{ log.error("User "+userId+"  Not Found");
                       throw  new UserNotFoundException();});

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
         throw new UserNotFoundException();

    }



    private String orderIdGenerator() {
        return String.format("%09d", (new Date().getTime()) % 1000000000)+ RandomStringUtils.randomNumeric(4);
    }


    public List<UserDetails> findAllUsers() {
        List<User> userList = userRepository.findAll()
                .stream().filter(user -> user.getStatus().equals(true)).collect(Collectors.toList());

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


    public String updateUserInfo(String userId, ChangeUserInfo changeUserInfo) {
        User user = getUser(userId);

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
            return userId;
        }
        log.error("User "+userId+" Not Found");
        throw new UserNotFoundException();
    }

    private User getUser(String userId) {
        return  Optional.ofNullable(userRepository.findUserByUserId(userId))
                    .orElseThrow(()->{log.error("User "+userId+" Not Found");
                       throw  new UserNotFoundException();});
    }

    public String deleteUserInfo(String userId) {
        User user = getUser(userId);
        if(user.getStatus()) {
            user.setStatus(false);

           List<Task> tasks = taskRepository.findTasksByUserId(user.getUserId());
           tasks.forEach(task -> task.setStatus(false));
           taskRepository.saveAll(tasks);

           userRepository.save(user);
            return userId;
        }
        log.error("User "+ userId +" Not Found");
        throw new UserNotFoundException();
    }

}
