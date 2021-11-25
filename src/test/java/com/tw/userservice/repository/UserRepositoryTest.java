package com.tw.userservice.repository;

import com.tw.userservice.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Rollback
@RunWith(SpringRunner.class)
public class UserRepositoryTest {
    private User user1;

    private User user2;

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp(){
        entityManager.clear();

        user1=User.builder()
                .id(1L)
                .name("小明")
                .userId("1234567891011")
                .cellphone("15229928145")
                .role("user")
                .status(true)
                .email("12345@qq.com")
                .age(10)
                .address("陕西省西安市")
                .build();

        user2=User.builder()
                .id(1L)
                .name("小红")
                .userId("1234567891012")
                .cellphone("15229928146")
                .role("user")
                .status(true)
                .address("山西省太原市")
                .email("123456@qq.com")
                .age(11)
                .build();

        entityManager.persistAndFlush(user1);
        entityManager.persistAndFlush(user2);
    }


    @Test
    public void should_return_user_if_userId_exists(){

        User result = userRepository.findUserByUserId("1234567891011");
        Assertions.assertEquals(user1.getId(),result.getId());

    }

    @Test
    public void should_return_user_list(){

        List<User> list = userRepository.findAll();

        Assertions.assertEquals(list.size(),2);
        Assertions.assertEquals(list.get(0),user1);

    }












}
