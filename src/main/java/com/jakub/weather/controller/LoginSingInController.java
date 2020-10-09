package com.jakub.weather.controller;

import com.jakub.weather.model.weather.authorization.AuthorizationRequest;
import com.jakub.weather.model.weather.user.UserEntity;
import com.jakub.weather.service.LoginService;
import com.jakub.weather.service.UserService;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/login")
@CrossOrigin("http://localhost:3000")
public class LoginSingInController {

    private UserService service;
    private LoginService loginService;

    public LoginSingInController(UserService service, LoginService loginService) {
        this.service = service;
        this.loginService = loginService;
    }

    @PostMapping("/signIn")
    public ResponseEntity<UserEntity> singIn(@RequestBody UserEntity user){

        UserEntity newUser = service.createNewUser(user);

        return ResponseEntity.ok(newUser);
    }

    @SneakyThrows
    @PostMapping
    public ResponseEntity<HttpServletResponse> login(@RequestBody AuthorizationRequest authRequest, HttpServletResponse response){
        loginService.authorization(authRequest);
        response.setHeader("Location", "http://localhost:3000/Home");
        return ResponseEntity.ok(response);
    }
}
