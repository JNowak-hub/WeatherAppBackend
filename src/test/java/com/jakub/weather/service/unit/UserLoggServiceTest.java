package com.jakub.weather.service.unit;

import com.jakub.weather.exceptions.UserNotFoundException;
import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.repo.UserLoggEntityRepo;
import com.jakub.weather.service.UserLoggService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserLoggServiceTest {
    @Mock
    private UserLoggEntityRepo loggEntityRepo;
    @InjectMocks
    private UserLoggService loggService;
    @Test
    void when_loggLogin_then_invokeLoggRepo_save(){
        //given
        UserEntity userEntity = new UserEntity("user", "user");
        //when
        loggService.loggLogin(userEntity);
        //then
        verify(loggEntityRepo).save(any());
    }


    @Test
    void when_loggLogin_withNoUser_then_throwUserNotFoundException(){
        //given
        UserEntity userEntity = null;
        //when
        UserNotFoundException myException = assertThrows(UserNotFoundException.class, () -> loggService.loggLogin(userEntity));
        //then
        assertThat(myException.getMessage()).isEqualTo("User cannot be null");
    }
    @Test
    void when_logOut_then_invokeLoggRepo_save(){
        //given
        UserEntity userEntity = new UserEntity("user", "user");
        //when
        loggService.logLogout(userEntity);
        //then
        verify(loggEntityRepo).save(any());
    }
    @Test
    void when_logOut_withNoUser_then_throwUserNotFoundException(){
        //given
        UserEntity userEntity = null;
        //when
        UserNotFoundException myException = assertThrows(UserNotFoundException.class, () -> loggService.logLogout(userEntity));
        //then
        assertThat(myException.getMessage()).isEqualTo("User cannot be null");
    }
    @Test
    void when_userCreated_then_invokeLoggRepo_save(){
        //given
        UserEntity userEntity = new UserEntity("user", "user");
        //when
        loggService.userCreated(userEntity);
        //then
        verify(loggEntityRepo).save(any());
    }
    @Test
    void when_userCreated_withNoUser_then_throwUserNotFoundException(){
        //given
        UserEntity userEntity = null;
        //when
        UserNotFoundException myException = assertThrows(UserNotFoundException.class, () -> loggService.userCreated(userEntity));
        //then
        assertThat(myException.getMessage()).isEqualTo("User cannot be null");
    }



}
