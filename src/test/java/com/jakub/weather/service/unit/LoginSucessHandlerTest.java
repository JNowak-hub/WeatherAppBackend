package com.jakub.weather.service.unit;

import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.service.LoginSucessHandler;
import com.jakub.weather.service.UserLoggService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LoginSucessHandlerTest {

    @Mock
    private UserLoggService loggService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private LoginSucessHandler loginSucessHandler;
    Authentication auth;
    private UserEntity user = new UserEntity("username", "password");

    @BeforeEach
    void setUpContextHolder() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), grantedAuthorities);
    }

    @Test
    void when_onAuthenticationSuccess_thenLoggServiceloggLogin() throws ServletException, IOException {
        //given
        setUpContextHolder();
        //when
        loginSucessHandler.onAuthenticationSuccess(request,response,auth);
        //then
        verify(loggService).loggLogin(any());
    }

}
