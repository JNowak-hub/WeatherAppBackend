package com.jakub.weather.service.unit;

import com.jakub.weather.exceptions.UserNotFoundException;
import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.service.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest

public class UserDetailsServiceImplTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void when_loadUserByUserName_then_userServiceFindUserByUsername(){
        //given
        UserEntity user = new UserEntity("username", "password");
        when(userService.findUserByUsername(user.getUsername())).thenReturn(user);
        //when
        UserEntity loadedUser = (UserEntity) userDetailsService.loadUserByUsername(user.getUserName());
        //then
        assertThat(loadedUser).isEqualTo(user);
    }

    @Test
    void when_loadUserByUserName_then_throwUserNotFoundException(){
        //given /when
        when(userService.findUserByUsername(anyString())).thenThrow(UserNotFoundException.class);
        //then
        assertThrows( UserNotFoundException.class,() -> userDetailsService.loadUserByUsername("username"));
    }

}
