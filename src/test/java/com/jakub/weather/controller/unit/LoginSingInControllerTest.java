package com.jakub.weather.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakub.weather.configuration.CustomExceptionHandler;
import com.jakub.weather.controller.LoginSingInController;
import com.jakub.weather.exceptions.UserAlreadyExists;
import com.jakub.weather.exceptions.WrongInputException;
import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.service.LoginService;
import com.jakub.weather.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
public class LoginSingInControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;
    @Mock
    private LoginService loginService;
    @InjectMocks
    private LoginSingInController controller;
    @BeforeEach
    void mockSetup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();
    }

    @Test
    void when_singIn_then_returnResponseOkAndUser() throws Exception {
        UserEntity user = new UserEntity("username", "password");
        when(userService.createNewUser(user)).thenReturn(user);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/api/auth/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void when_singIn_then_throwUserAlreadyExistsException() throws Exception {
        UserEntity user = new UserEntity("username", "password");
        when(userService.createNewUser(any())).thenThrow(UserAlreadyExists.class);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/api/auth/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isConflict());
    }

    @Test
    void when_singIn_then_throwWrongInputException() throws Exception {
        UserEntity user = new UserEntity("username", "password");
        when(userService.createNewUser(any())).thenThrow(WrongInputException.class);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/api/auth/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

//    @Test
//    void when_login_then_returnServletResponse() throws Exception {
//        mockSetup();
//        AuthorizationRequest authRequest = new AuthorizationRequest();
//        authRequest.setUserName("username");
//        authRequest.setPassword("password");
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        mockMvc.perform(post("/api/auth/login")
//        .contentType(MediaType.APPLICATION_JSON)
//        .content(objectMapper.writeValueAsString(authRequest)))
//                .andExpect(ResultMatcher.matchAll());
//    }


}
