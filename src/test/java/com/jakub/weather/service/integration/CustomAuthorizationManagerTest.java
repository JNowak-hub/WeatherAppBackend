package com.jakub.weather.service.integration;

import com.jakub.weather.configuration.CustomAuthorizationManager;
import com.jakub.weather.exceptions.UserNotFoundException;
import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class CustomAuthorizationManagerTest {

    @Autowired
    CustomAuthorizationManager authorizationManager;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;

    private UserEntity user = new UserEntity();
    private final String USER_PASSWORD = "password";
    private final String USERNAME = "username";

    @BeforeEach
    void init() {
        user.setPassword(USER_PASSWORD);
        user.setUserName(USERNAME);
        userService.createNewUser(user);
    }
    @AfterEach
    void cleanUp(){
        userService.deleteUserByUserName(user.getUserName());
    }

    @Test
    public void Given_Authentication_When_authenticate_Then_Authenticate() {
        //given
        Authentication userToAuthenticate = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        //when
        Authentication authentication = authorizationManager.authenticate(userToAuthenticate);
        //than
        assertThat(authentication.isAuthenticated()).isTrue();
    }

    @Test
    public void Given_Wrong_UserName_When_authenticate_Then_Throw_UserNotFoundException() {
        Authentication userToAuthenticate = new UsernamePasswordAuthenticationToken("WrongUserName", user.getPassword());
        //when
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> authorizationManager.authenticate(userToAuthenticate));
        //than
        assertThat(userNotFoundException.getMessage()).isEqualTo("User : " + "WrongUserName" + " doesn't exists");

    }

    @Test
    public void Given_Wrong_Password_When_authenticate_Then_Throw_BadCredentialsException() {
        Authentication userToAuthenticate = new UsernamePasswordAuthenticationToken(user.getUserName(), "wrongPassword");
        //when
        BadCredentialsException userNotFoundException = assertThrows(BadCredentialsException.class, () -> authorizationManager.authenticate(userToAuthenticate));
        //than
        assertThat(userNotFoundException.getMessage()).isEqualTo("Bad credentials");
    }
}
