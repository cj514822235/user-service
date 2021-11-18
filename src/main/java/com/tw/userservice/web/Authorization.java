package com.tw.userservice.web;

import com.tw.userservice.exception.UserNotFoundException;
import com.tw.userservice.modle.User;
import com.tw.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Authorization {
    @Autowired
    private UserRepository  userRepository;

    public Boolean getAuthorization(String userId){
        User user = Optional.ofNullable(userRepository.findUserByUserId(userId))
                .orElseThrow(()-> new UserNotFoundException("User "+userId+"Not Found"));
        return user.getRole().equals("admin");
    }

}
