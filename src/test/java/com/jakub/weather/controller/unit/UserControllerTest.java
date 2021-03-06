package com.jakub.weather.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakub.weather.configuration.CustomExceptionHandler;
import com.jakub.weather.controller.UserController;
import com.jakub.weather.exceptions.UserNotFoundException;
import com.jakub.weather.exceptions.WrongInputException;
import com.jakub.weather.model.dto.UserEntityRequest;
import com.jakub.weather.model.dto.UserSettingRequest;
import com.jakub.weather.model.user.Role;
import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.service.UserService;
import com.jakub.weather.service.UserSettingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;
    @Mock
    private UserSettingsService settingsService;
    @InjectMocks
    private UserController controller;

    @BeforeEach
    void mockSetup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();
    }

    @Test
    void when_updateUserSettings_returnStatusOk() throws Exception {
        UserSettingRequest request = new UserSettingRequest();
        request.setDaysAmount(2L);
        request.setDefaultCity("Krakow");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/api/user/update/Settings")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.defaultCity", is("Krakow")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.daysAmount", is(2)));
    }

    @Test
    void when_updateUserSettings_throwUserNotFoundException() throws Exception {
        UserSettingRequest request = new UserSettingRequest();
        request.setDaysAmount(2L);
        request.setDefaultCity("Krakow");
        doThrow(UserNotFoundException.class).when(settingsService).updateUserSettings(any());
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/api/user/update/Settings")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
    @Test
    void when_updateUserSettings_throwWrongInputException() throws Exception {
        UserSettingRequest request = new UserSettingRequest();
        request.setDaysAmount(2L);
        request.setDefaultCity("Krakow");
        doThrow(WrongInputException.class).when(settingsService).updateUserSettings(any());
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/api/user/update/Settings")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void when_updateUser_returnUserUpdated() throws Exception {
        UserEntityRequest userEntityRequest = new UserEntityRequest();
        userEntityRequest.setUserName("newUserName");
        userEntityRequest.setPassword("newPassword");
        UserEntity updatedUser = new UserEntity(userEntityRequest.getUserName(), userEntityRequest.getPassword());
        updatedUser.setRole(Collections.singletonList(new Role("User")));
        when(userService.updateUser(any())).thenReturn(updatedUser);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/api/user/update/user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userEntityRequest)))
                .andExpect(status().isOk())
        .andExpect(content().string("User updated"));
    }

    @Test
    void when_updateUser_throwUserNotFoundException() throws Exception {
        UserEntityRequest userEntityRequest = new UserEntityRequest();
        userEntityRequest.setUserName("newUserName");
        userEntityRequest.setPassword("newPassword");
        when(userService.updateUser(any())).thenThrow(UserNotFoundException.class);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/api/user/update/user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userEntityRequest)))
                .andExpect(status().isNotFound());
    }
}
