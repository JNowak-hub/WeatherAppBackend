package com.jakub.weather.controller;

import com.jakub.weather.model.weather.dto.UserSettingRequest;
import com.jakub.weather.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/update/Settings")
    public ResponseEntity.BodyBuilder updateUserSettings(@RequestBody UserSettingRequest request){
        userService.updateUserSettings(request);
        return ResponseEntity.status(HttpStatus.OK);
    }
}
