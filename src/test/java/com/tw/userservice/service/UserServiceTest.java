package com.tw.userservice.service;

import com.tw.userservice.exception.UserNotFoundException;
import com.tw.userservice.model.ChangeUserInfo;
import com.tw.userservice.model.Level;
import com.tw.userservice.model.Task;
import com.tw.userservice.model.User;
import com.tw.userservice.model.UserDetails;
import com.tw.userservice.repository.TaskRepository;
import com.tw.userservice.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskRepository taskRepository;
    private User user;
    private User user1;
    private Task task;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository,taskRepository);
        user = User.builder()
                .id(1L)
                .userId("1234567891011")
                .name("小红")
                .cellphone("15228829245")
                .status(true)
                .role("admin")
                .email("514822235@qq.com")
                .age(10)
                .address("陕西省西安市")
                .build();

        user1 = User.builder()
                .id(2L)
                .name("小名")
                .status(true)
                .userId("1234567891012")
                .role("user")
                .email("12345@qq.com")
                .age(11)
                .cellphone("12345678")
                .address("山西省太原市")
                .build();
       task= Task.builder()
               .id(1L)
               .status(true)
               .description("add task")
               .level(Level.EASY)
               .userId(user.getUserId())
               .build();


    }
    @Test
    public void should_return_right_userId_when_create_user() {
        Mockito.when(userRepository.save(user)).thenReturn(user);

        String userId = userService.createUser(user);

        Assertions.assertEquals(userId,user.getUserId());
        Mockito.verify(userRepository).save(user);
    }

    @Test
    public void should_return_right_user_when_find_by_userId(){
        Mockito.when(userRepository.findUserByUserId("1234567891011")).thenReturn(user);

         UserDetails userDetails = userService.findByUserId("1234567891011");

         Assertions.assertEquals(userDetails.getUserId(),user.getUserId());
         Assertions.assertEquals(userDetails.getAddress(),user.getAddress());
         Assertions.assertEquals(userDetails.getName(),user.getName());


    }
    @Test
    public void should_throw_exception_when_userId_not_found(){

        Mockito.when(userRepository.findUserByUserId("1234567891011")).thenReturn(user);


        UserNotFoundException userNotFoundException= Assertions.assertThrows(UserNotFoundException.class, () ->
            userService.findByUserId("1234567891012")
        );

        Assertions.assertTrue(userNotFoundException.getMessage().contains("User not found"));
    }

    @Test
    public void should_return_all_users_when_find_all_users(){
        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user1);
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        List<UserDetails> userDetailsList = userService.findAllUsers();

        Assertions.assertEquals(userDetailsList.get(0).getUserId(),user.getUserId());


    }
    @Test
    public void should_return_right_user_and_task_status_when_delete_user(){
     List<Task> tasks = new ArrayList<>();
     tasks.add(task);
     Mockito.when(userRepository.findUserByUserId("1234567891011")).thenReturn(user);
     Mockito.when(taskRepository.findTasksByUserId(user.getUserId())).thenReturn(tasks);


     String result = userService.deleteUserInfo("1234567891011");

     Assertions.assertEquals("1234567891011",result);
     Assertions.assertFalse(user.getStatus());
     Assertions.assertFalse(task.getStatus());
    }

    @Test
    public void should_return_right_user_info_when_update_user_info(){
        ChangeUserInfo changeUserInfo = ChangeUserInfo.builder()
                .address("上海虹桥")
                .age(100)
                .email("1234@qq.com")
                .cellphone("123456")
                .build();
        Mockito.when(userRepository.findUserByUserId("1234567891011")).thenReturn(user);

        String updateUser = userService.updateUserInfo("1234567891011",changeUserInfo);

        Assertions.assertEquals("1234567891011",updateUser );
        Assertions.assertEquals(user.getAge(),changeUserInfo.getAge());
        Assertions.assertEquals(user.getCellphone(),changeUserInfo.getCellphone());


    }

    @Test
    public void should_return_right_user_status_when_delete_user(){
        Mockito.when(userRepository.findUserByUserId("1234567891011")).thenReturn(user);

        String deleteUser = userService.deleteUserInfo("1234567891011");

        Assertions.assertEquals("1234567891011",deleteUser);

        Assertions.assertFalse(user.getStatus());

    }





}
