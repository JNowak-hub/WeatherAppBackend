package com.jakub.weather.service;

import com.jakub.weather.model.weather.authorization.AuthorizationRequest;
import com.jakub.weather.model.weather.user.UserEntity;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class LoginService {

    private AuthenticationManager authenticationManager;

    public LoginService(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
    }

    public void authorization(AuthorizationRequest authorizationRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authorizationRequest.getUserName(),
                authorizationRequest.getPassword()));
    }
}
