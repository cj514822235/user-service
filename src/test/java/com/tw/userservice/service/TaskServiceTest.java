package com.tw.userservice.service;

import com.tw.userservice.exception.TaskNotFoundException;
import com.tw.userservice.model.Level;
import com.tw.userservice.model.Criteria;
import com.tw.userservice.model.ModifyTaskInfo;
import com.tw.userservice.model.ShareTask;
import com.tw.userservice.model.Task;
import com.tw.userservice.model.User;
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

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskRepository taskRepository;




    private User user;
    private Task firstTask;
    private Task secondTask;

    @BeforeEach
    void setUp() {

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

      firstTask = Task.builder()
                .id(1L)
                .status(true)
                .description("add task")
                .level(Level.EASY)
                .userId(user.getUserId())
                .build();

      secondTask = Task.builder()
                .id(2L)
                .status(true)
                .level(Level.HARD)
              .description("add tasks1")
                .userId(user.getUserId())
                .build();
    }

    @Test
    public void should_return_right_userId_when_create_task_for_user(){
        List<Task> tasks = new ArrayList<>();
        tasks.add(firstTask);
        tasks.add(secondTask);

        Mockito.when(userRepository.findUserByUserId("1234567891011")).thenReturn(user);

        String userId = taskService.createTasks(user.getUserId(),tasks);

        Assertions.assertEquals("1234567891011",userId);

    }

    @Test
    public void should_return_all_tasks_when_find_all_task(){
        List<Task> tasks = new ArrayList<>();
        tasks.add(firstTask);
        tasks.add(secondTask);
        Mockito.when(taskRepository.findAll()).thenReturn(tasks);

       List<Task> result = taskService.getAllTasks();

       Assertions.assertEquals(tasks,result);

    }

    @Test
    public void should_return_right_status_when_find_by_order_level(){
        List<Task> tasks = new ArrayList<>();
        tasks.add(firstTask);
        tasks.add(secondTask);

        Mockito.when(userRepository.findUserByUserId("1234567891011")).thenReturn(user);
        Mockito.when(taskRepository.findTasksByUserId(user.getUserId())).thenReturn(tasks);
     //   List<Task> result = taskService.getTasksByStatus("1234567891011","HARD");

      //  Assertions.assertEquals(Level.HARD,result.get(0).getLevel());

    }

    @Test
    public void should_return_all_task_for_user(){
        List<Task> tasks = new ArrayList<>();
        tasks.add(firstTask);

        TaskSpecification taskSpecification = Mockito.any();

        Mockito.when(taskRepository.findAll(taskSpecification)).thenReturn(tasks);


        List<Task> result = taskService.getTasks("1234567891011","EASY");

        Assertions.assertEquals(tasks,result);

    }

    @Test
    public void should_return_right_request(){


       Criteria request = taskService.getCriteria("1234567891011","EASY");

       Assertions.assertEquals(request.getUserId(),user.getUserId());
       Assertions.assertEquals(request.getLevel(),Level.EASY);
    }

    @Test
    public void should_return_right_task_when_user_get_task(){
        Mockito.when(taskRepository.findTaskById(1L)).thenReturn(firstTask);

        Task result = taskService.getTask(1L);

        Assertions.assertEquals(firstTask.getId(),result.getId());

    }


    @Test
    public void should_throw_exception_when_task_id_error(){
        Mockito.when(taskRepository.findTaskById(1L)).thenReturn(firstTask);

        TaskNotFoundException taskNotFoundException = Assertions.assertThrows(TaskNotFoundException.class,()->
                taskService.getTask(3L));
        Assertions.assertTrue(taskNotFoundException.getMessage().contains("Task Not Found"));

    }



//    @Test
//    public void should_throw_exception_when_userId_not_found(){
//        List<Task> tasks = new ArrayList<>();
//        tasks.add(firstTask);
//        tasks.add(secondTask);
//        Mockito.when(userRepository.findUserByUserId("1234567891011")).thenReturn(user);
//        Mockito.when(taskRepository.findTasksByUserId(user.getUserId()).thenReturn(tasks);
//        String userId = "1234567891013";
//
//
//
//   //    UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class,()->
//     //           taskService.getTasksByStatus(userId,"HARD")
//    //            );
//    //   Assertions.assertTrue(userNotFoundException.getMessage().contains("User "+"1234567891013"+" Not Found"));
//
//    }

    @Test
    public void should_throw_exception_when_task_id_not_found(){
        List<Task> tasks = new ArrayList<>();
        tasks.add(firstTask);
        tasks.add(secondTask);
        Mockito.when(userRepository.findUserByUserId("1234567891011")).thenReturn(user);
        Mockito.when(taskRepository.findTasksByUserId(user.getUserId())).thenReturn(tasks);


        TaskNotFoundException taskNotFoundException = Assertions.assertThrows(TaskNotFoundException.class,()->
                taskService.deleteTask(3L)
        );
        Assertions.assertTrue(taskNotFoundException.getMessage().contains("Task "+3L+" Not Found"));


    }

    @Test
    public void should_return_right_task_status_when_delete_task(){
        Mockito.when(userRepository.findUserByUserId("1234567891011")).thenReturn(user);
        Mockito.when(taskRepository.findTaskById(1L)).thenReturn(firstTask);

        String result = taskService.deleteTask(1L);

        Assertions.assertEquals("successfully delete",result);
        Assertions.assertFalse(firstTask.getStatus());
    }

    @Test
    public void should_return_right_task_id_when_sharing_task(){
        ShareTask shareTask = ShareTask.builder()
                .taskId(1L)
                .userId("1234567891011")
                .build();

        Mockito.when(taskRepository.findTaskById(shareTask.getTaskId())).thenReturn(firstTask);

        Long taskId = taskService.shareTask(shareTask);

        Assertions.assertNull(taskId);

    }

    @Test
    public void should_return_right_task_id_when_modify_task(){
        ModifyTaskInfo modifyTaskInfo = ModifyTaskInfo.builder().description("modifyTask").build();

        Mockito.when(taskRepository.findTaskById(1L)).thenReturn(firstTask);

        Long taskId = taskService.modifyTask(1L,modifyTaskInfo);

        Assertions.assertEquals(1L,taskId);

    }
    @Test
    public void should_throw_exception_when_task_id_not_exist(){
        ModifyTaskInfo modifyTaskInfo = ModifyTaskInfo.builder().description("modifyTask").build();

        Mockito.when(taskRepository.findTaskById(1L)).thenReturn(firstTask);

        TaskNotFoundException taskNotFoundException = Assertions.assertThrows(TaskNotFoundException.class,

                ()-> taskService.modifyTask(2L,modifyTaskInfo));
        Assertions.assertTrue(taskNotFoundException.getMessage().contains("Task Not Found"));
    }








}
