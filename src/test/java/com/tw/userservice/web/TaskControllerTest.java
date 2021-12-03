package com.tw.userservice.web;


import com.google.gson.Gson;
import com.tw.userservice.model.Level;
import com.tw.userservice.model.ModifyTaskInfo;
import com.tw.userservice.model.ShareTask;
import com.tw.userservice.model.Task;
import com.tw.userservice.model.TaskDto;
import com.tw.userservice.model.User;

import com.tw.userservice.service.TaskService;
import com.tw.userservice.utils.Authorization;
import com.tw.userservice.utils.Header;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureJsonTesters
public class TaskControllerTest {
    @MockBean
    private TaskService taskService;

    @MockBean
    private Authorization authorization;

    @MockBean
    private Header header;
    @Autowired
    private MockMvc mockMvc;

    private TaskDto taskDto;

    @Autowired
    JacksonTester<ShareTask> shareTaskJacksonTester;

    @Autowired
    JacksonTester<ModifyTaskInfo> modifyTaskInfoJacksonTester;

    private Task firstTask;

    private Task secondTask;

    private User firstUser;


    @BeforeEach
    public void setUp() {
        firstUser = User.builder()
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

        firstTask = Task.builder()
                .status(null)
                .level(Level.EASY)
                .description("add first task")
                .userId("1234567891011")
                .id(null)
                .build();

        secondTask = Task.builder()
                .status(null)
                .id(null)
                .userId("1234567891011")
                .description("add second task")
                .level(Level.HARD)
                .build();
        List<Task> taskList = new ArrayList<>();
        taskList.add(firstTask);
        taskList.add(secondTask);

        taskDto = TaskDto.builder()
                .tasks(taskList)
                .userId(firstUser.getUserId())
                .build();

    }

    @Test
    public void should_create_new_tasks_and_return_its_id() throws Exception {

        Gson gson = new Gson();
        String json = gson.toJson(taskDto);

        Mockito.when(taskService.createTasks("1234567891011", taskDto.getTasks())).thenReturn("1234567891011");
        Mockito.when(authorization.authorize("MTIzNDU2Nzg5MTAxMQ==", "1234567891011")).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                        .header("token","MTIzNDU2Nzg5MTAxMQ==")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(taskDto.getUserId())));

        verify(taskService).createTasks(taskDto.getUserId(), taskDto.getTasks());


    }

    @Test
    public void should_return_right_tasks_when_find_by_different_condition() throws Exception {
        List<Task> result = new ArrayList<>();
        result.add(firstTask);
        Mockito.when(taskService.getTasks("1234567891011", "EASY")).thenReturn(result);
        Mockito.when(authorization.authorize("MTIzNDU2Nzg5MTAxMQ==", "1234567891011")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks")
                        .header("token", "MTIzNDU2Nzg5MTAxMQ==")
                        .param("userId", "1234567891011")
                        .param("level", "EASY")

                )
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].userId",is("1234567891011")))
//                .andExpect(jsonPath("$[1].userId",is("1234567891012")))
                .andExpect(jsonPath("$[0].userId", is("1234567891011")))
                .andExpect(jsonPath("$[0].level", is("EASY")));

        verify(taskService).getTasks("1234567891011", "EASY");
    }

    @Test
    public void should_return_task_when_find_by_id() throws Exception {
        Task task = Task.builder()
                .level(Level.EASY)
                .description("add task")
                .userId("1234567891011")
                .id(1L)
                .status(true)
                .build();

        Mockito.when(taskService.getTask(1L)).thenReturn(task);
        Mockito.when(authorization.authorizeIsAdmin("MTIzNDU2Nzg5MTAxMQ==")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{id}", 1L)
                        .header("token", "MTIzNDU2Nzg5MTAxMQ=="))


                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is("1234567891011")))
                .andExpect(jsonPath("$.level", is("EASY")))
                .andExpect(jsonPath("$.description", is("add task")));

        verify(taskService).getTask(1L);
    }

    @Test
    public void should_return_right_status_when_delete_task() throws Exception {
        Mockito.when(taskService.deleteTask(1L)).thenReturn(1L);
        Mockito.when(authorization.authorizeIsAdmin("MTIzNDU2Nzg5MTAxMQ==")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/{id}", 1L)
                        .header("token", "MTIzNDU2Nzg5MTAxMQ=="))


                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1")));

        verify(taskService).deleteTask(1L);
    }

    @Test
    public void should_throw_exception_when_token_error() throws Exception {
        Task task = Task.builder()
                .level(Level.EASY)
                .description("add task")
                .userId("1234567891011")
                .id(1L)
                .status(true)
                .build();

        Mockito.when(taskService.getTask(1L)).thenReturn(task);
        Mockito.when(authorization.authorizeIsAdmin("MTIzNDU2Nzg5MTAxMQ==")).thenReturn(true);


        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{id}", 1L)
                        .header("token", "MTIzNDU2Nzg5MTAxM=="))


                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("No Authorization")));
    }

    @Test
    public void should_throw_exception_when_delete_task_token_error() throws Exception {
        Mockito.when(taskService.deleteTask(1L)).thenReturn(1L);
        Mockito.when(authorization.authorizeIsAdmin("MTIzNDU2Nzg5MTAxMQ==")).thenReturn(true);
        Mockito.when(header.parsingToken("MTIzNDU2Nzg5MTAxM==")).thenReturn("1234567891011");

        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/{id}", 1L)
                        .header("token", "MTIzNDU2Nzg5MTAxM=="))


                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("USER 1234567891011 NO AUTHORIZATION")));


    }

    @Test
    public void should_throw_exception_when_find_by_different_condition_token_error() throws Exception {
        List<Task> result = new ArrayList<>();
        result.add(firstTask);
        Mockito.when(taskService.getTasks("1234567891011", "EASY")).thenReturn(result);
        Mockito.when(authorization.authorize("MTIzNDU2Nzg5MTAxMQ==", "1234567891011")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks")
                        .header("token", "MTIzNDU2Nzg5MTAxM==")
                        .param("userId", "1234567891011")
                        .param("level", "EASY")

                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("No Authorization")));
    }
    @Test
    public void should_return_task_id_when_sharing_task() throws Exception{
        ShareTask shareTask = ShareTask.builder()
                .taskId(1L)
                .userId("1234567891011")
                .build();

       // JacksonTester<ShareTask> shareTaskJacksonTester = null;

        Mockito.when(taskService.shareTask(shareTask)).thenReturn(2L);
        Mockito.when(authorization.authorize("MTIzNDU2Nzg5MTAxMQ==", 1L,"1234567891011")).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/sharing")
                        .header("token", "MTIzNDU2Nzg5MTAxMQ==")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shareTaskJacksonTester.write(shareTask).getJson())


                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("2")));
    }


    @Test
    public void should_throw_exception_when_sharing_task_token_error() throws Exception{
        ShareTask shareTask = ShareTask.builder()
                .taskId(1L)
                .userId("1234567891011")
                .build();


        Mockito.when(taskService.shareTask(shareTask)).thenReturn(2L);
        Mockito.when(authorization.authorize("MTIzNDU2Nzg5MTAxMQ==", 1L)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/sharing")
                        .header("token", "MTIzNDU2Nzg5MTAxM==")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shareTaskJacksonTester.write(shareTask).getJson())


                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("No Authorization")));
    }
    @Test
    public void should_return_right_task_id_when_modify_task() throws Exception{
        ModifyTaskInfo modifyTaskInfo = ModifyTaskInfo.builder().description("modifyTask").build();

        Mockito.when(taskService.modifyTask(1L,modifyTaskInfo)).thenReturn(1L);
        Mockito.when(authorization.authorize("MTIzNDU2Nzg5MTAxMQ==", 1L)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/{id}",1L)
                        .header("token", "MTIzNDU2Nzg5MTAxMQ==")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(modifyTaskInfoJacksonTester.write(modifyTaskInfo).getJson())


                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1")));
    }


    @Test
    public void should_throw_exception_when_modify_task_token_error() throws Exception{
       ModifyTaskInfo modifyTaskInfo = ModifyTaskInfo.builder().description("modifyTask").build();

        Mockito.when(taskService.modifyTask(1L,modifyTaskInfo)).thenReturn(1L);
        Mockito.when(authorization.authorize("MTIzNDU2Nzg5MTAxMQ==", 1L,"1234567891011")).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/{id}",1L)

                        .header("token", "MTIzNDU2Nzg5MTAxM==")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(modifyTaskInfoJacksonTester.write(modifyTaskInfo).getJson())


                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("No Authorization")));
    }

}
