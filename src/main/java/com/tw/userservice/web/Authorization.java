package com.tw.userservice.web;

import com.tw.userservice.modle.User;
import com.tw.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Authorization {
    @Autowired
    private UserRepository  userRepository;

    public Boolean getAuthorization(String userId){
        User user = userRepository.findUserByUserId(userId);
        return user.getRole().equals("admin");
    }

}
