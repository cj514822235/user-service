package com.tw.userservice.repository;

import com.tw.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findUserByUserId(String userId);
   // User findUserById(Long Id);
    User findUserByCellphone(String phoneNumber);

    @Override
    List<User> findAll();
}
