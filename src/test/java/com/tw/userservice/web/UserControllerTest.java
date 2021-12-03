package com.tw.userservice.web;

import com.tw.userservice.model.ChangeUserInfo;
import com.tw.userservice.model.User;
import com.tw.userservice.model.UserDetails;

import com.tw.userservice.service.UserService;
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

@WebMvcTest(UserController.class)

@AutoConfigureJsonTesters
public class UserControllerTest {
     @MockBean
     private UserService userService;

     @MockBean
     private Authorization authorization;

     @MockBean
     private Header header;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<ChangeUserInfo> changeUserInfoJacksonTester;

    @Autowired
    private JacksonTester<User> userJacksonTester;

    private User firstUser;

    private User secondUser;

    private UserDetails firstUserDetails;

    private UserDetails secondUserDetails;

    @BeforeEach
    public void beforeEach() {
        firstUser = User.builder()
                .id(null)
                .name("小明")
                .userId("1234567891011")
                .cellphone("15229928145")
                .role("user")
                .status(true)
                .email("12345@qq.com")
                .age(10)
                .address("陕西省西安市")
                .build();

        secondUser = User.builder()
                .id(null)
                .name("红")
                .userId("1234567891012")
                .cellphone("1522992")
                .role("user")
                .status(true)
                .email("12345@qq.com")
                .age(10)
                .address("陕西省西安市")
                .build();

        firstUserDetails = UserDetails.builder()
                .address(firstUser.getAddress())
                .name(firstUser.getName())
                .age(firstUser.getAge())
                .email(firstUser.getEmail())
                .userId(firstUser.getUserId())
                .cellphone(firstUser.getCellphone())
                .build();

        secondUserDetails = UserDetails.builder()
                .name(secondUser.getName())
                .userId(secondUser.getUserId())
                .email(secondUser.getEmail())
                .age(secondUser.getAge())
                .address(secondUser.getAddress())
                .cellphone(secondUser.getCellphone())
                .build();


    }
//    @AfterEach
//    public void afterEach() {
//        Mockito.reset(userService);
//    }

    @Test
    public void should_return_user_by_id_with_jsonPath() throws Exception{
       Mockito.when(userService.findByUserId("1234567891011")).thenReturn(firstUserDetails);
       Mockito.when(authorization.authorize("MTIzNDU2Nzg5MTAxMQ==","1234567891011")).thenReturn(true);



        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}","1234567891011")
                        .header("token","MTIzNDU2Nzg5MTAxMQ==")
                )


                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId",is("1234567891011")))
                .andExpect(jsonPath ("$.name", is("小明")));

        verify(userService).findByUserId("1234567891011");

    }

    @Test
    public void should_return_users_when_get_all_user() throws Exception {

        List<UserDetails> users = new ArrayList<>();
        users.add(firstUserDetails);
        users.add(secondUserDetails);
        Mockito.when(userService.findAllUsers()).thenReturn(users);
        Mockito.when(authorization.authorizeIsAdmin("MTIzNDU2Nzg5MTAxMQ==")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .header("token","MTIzNDU2Nzg5MTAxMQ==")
                )

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].userId",is("1234567891011")))
                .andExpect(jsonPath("$[1].userId",is("1234567891012")));

    }
    @Test
    public void should_throw_exception_when_get_all_user_token_error() throws Exception {

        List<UserDetails> users = new ArrayList<>();
        users.add(firstUserDetails);
        users.add(secondUserDetails);
        Mockito.when(userService.findAllUsers()).thenReturn(users);
        Mockito.when(authorization.authorizeIsAdmin("MTIzNDU2Nzg5MTAxMQ==")).thenReturn(true);
        Mockito.when(header.parsingToken("MTIzNDU2Nzg5MTAxM==")).thenReturn("1234567891011");

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .header("token","MTIzNDU2Nzg5MTAxM==")
                )

                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("USER 1234567891011 NO AUTHORIZATION")));
    }

    @Test
    public void should_throw_exception_when_token_error() throws Exception {

        Mockito.when(userService.findByUserId("1234567891011")).thenReturn(firstUserDetails);
        Mockito.when(authorization.authorize("MTIzNDU2Nzg5MTAxMQ==","1234567891011")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}","1234567891011")
                        .header("token","MTIzNDU2Nzg5MTMQ==")
                )

                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath ("$.message", is("Token Error")));

    }
    @Test
    public void should_return_right_user_info_when_update_user_info() throws Exception{
        ChangeUserInfo changeUserInfo = ChangeUserInfo.builder()
                .address("上海")
                .age(99)
                .email("thoughtworks.com")
                .cellphone("123456")
                .build();

        Mockito.when(userService.updateUserInfo(firstUser.getUserId(),changeUserInfo))
                .thenReturn("User "+firstUser.getUserId()+" Info Update");

        Mockito.when(authorization.authorize("MTIzNDU2Nzg5MTAxMQ==","1234567891011")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}","1234567891011")
                        .header("token","MTIzNDU2Nzg5MTAxMQ==")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(changeUserInfoJacksonTester.write(changeUserInfo).getJson())
                )

                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("User "+firstUser.getUserId()+" Info Update")));

        verify(userService).updateUserInfo(firstUser.getUserId(),changeUserInfo);
    }

    @Test
    public void should_return_right_message_when_delete_user() throws Exception{

        Mockito.when(userService.deleteUserInfo("1234567891011")).thenReturn("successfully delete");
        Mockito.when(authorization.authorize("MTIzNDU2Nzg5MTAxMQ==","1234567891011")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}","1234567891011")
                        .header("token","MTIzNDU2Nzg5MTAxMQ==")
                )

                .andExpect(status().isOk())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("successfully delete")));
        verify(userService).deleteUserInfo("1234567891011");

    }
    @Test
    public void should_return_user_id_when_create_user() throws Exception{
        Mockito.when(userService.createUser(firstUser)).thenReturn(firstUser.getUserId());

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJacksonTester.write(firstUser).getJson())
        )

                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(firstUser.getUserId())));
    }
    @Test
    public void should_throw_exception_when_delete_user_token_error() throws Exception {

        Mockito.when(userService.deleteUserInfo("1234567891011")).thenReturn("successfully delete");
        Mockito.when(authorization.authorize("MTIzNDU2Nzg5MTAxMQ==", "1234567891011")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}", "1234567891011")
                        .header("token", "MTIzNDU2Nzg5MTAxM==")
                )

                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Token Error")));
    }



}
