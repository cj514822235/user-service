package com.tw.userservice.service;


import com.tw.userservice.modle.User;
import com.tw.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService( UserRepository userRepository) {

        this.userRepository = userRepository;

    }
    public void createUser(User user) {
        userRepository.save(user);
    }





}
