package com.jakub.weather.service.unit;
import com.jakub.weather.configuration.CustomAuthorizationManager;
import com.jakub.weather.model.authorization.AuthorizationRequest;
import com.jakub.weather.service.LoginService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LoginServiceTest {
    @Mock
    private CustomAuthorizationManager authorizationManager;
    @InjectMocks
    private LoginService loginService;
    @Test
    void when_authorization_then_invoke_authenticationManager_authenticate(){
        //given
        AuthorizationRequest authRequest = new AuthorizationRequest();
        authRequest.setPassword("password");
        authRequest.setUserName("userName");
        //when
        loginService.authorization(authRequest);
        //then
        verify(authorizationManager).authenticate(any());
    }

    @Test
    void when_authorization_then_throw_BadCredentialsException(){
        //given
        when(authorizationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));
        AuthorizationRequest authRequest = new AuthorizationRequest();
        authRequest.setPassword("password");
        authRequest.setUserName("userName");
        //when
        BadCredentialsException badCredentialsException = assertThrows(BadCredentialsException.class, () -> loginService.authorization(authRequest));
        //then
        assertThat(badCredentialsException.getMessage()).isEqualTo("Bad credentials");
    }
}
