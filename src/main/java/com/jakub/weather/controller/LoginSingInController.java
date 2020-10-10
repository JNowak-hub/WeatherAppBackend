package com.jakub.weather.controller;

import com.jakub.weather.model.authorization.AuthorizationRequest;
import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.service.LoginService;
import com.jakub.weather.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin({"http://localhost:3000","http://localhost:8080"})
public class LoginSingInController {


    private UserService service;
    private LoginService loginService;

    public LoginSingInController(UserService service, LoginService loginService) {
        this.service = service;
        this.loginService = loginService;
    }

    @ApiOperation(value = "Creates new account with username and password", notes = "Username and password needs to be provided here as JSON")
    @PostMapping("/signIn")
    public ResponseEntity<String> singIn(@RequestBody UserEntity user) {

        UserEntity newUser = service.createNewUser(user);

        return ResponseEntity.ok("user " + newUser.getUserName() + " created");
    }

    @ApiOperation(value = "Authorization of User based on username and password", notes = "Username and password needs to be provided here as JSON")
    @PostMapping("/login")
    public ResponseEntity<HttpServletResponse> login(@RequestBody AuthorizationRequest authRequest, HttpServletResponse response) {
        loginService.authorization(authRequest);
        response.addHeader("Location","http://localhost:3000/Home");
        return ResponseEntity.ok(response);
    }
}
