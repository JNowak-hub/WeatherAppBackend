package com.jakub.weather.service.integration;

import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.repo.UserRepo;
import com.jakub.weather.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserRepo userRepo;
    @Autowired
    UserService userService;

    private UserEntity user = new UserEntity();
    private final String USER_PASSWORD = "password";
    private final String USERNAME = "username";
    private final String ROLE = "USER";

    @BeforeEach
    void tearDown(){
        userRepo.deleteAll();
    }

    @Test
    void Given_User_When_createNewUser_Than_newUser_in_DataBase(){
        //given
        user.setPassword(USER_PASSWORD);
        user.setUserName(USERNAME);
        //When
        userService.createNewUser(user);
        //THan
        assertThat(userService.findUserByUsername(user.getUserName()).getUserName()).isEqualTo(user.getUserName());
    }
}
