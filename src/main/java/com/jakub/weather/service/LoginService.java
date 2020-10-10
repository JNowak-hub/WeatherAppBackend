package com.jakub.weather.service;

import com.jakub.weather.model.authorization.AuthorizationRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private AuthenticationManager authenticationManager;

    public LoginService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void authorization(AuthorizationRequest authorizationRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authorizationRequest.getUserName(),
                authorizationRequest.getPassword()));
    }
}
