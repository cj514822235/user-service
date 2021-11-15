package com.tw.userservice.repository;

import com.tw.userservice.modle.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface UserRepository extends JpaRepository<User, String> {
    User findUserByUserId(String userId);

    @Override
    List<User> findAll();
}
