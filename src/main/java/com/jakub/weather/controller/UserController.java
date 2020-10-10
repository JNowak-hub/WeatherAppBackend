package com.jakub.weather.controller;

import com.jakub.weather.model.dto.UserSettingRequest;
import com.jakub.weather.service.UserSettingsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserSettingsService userSettingsService;

    public UserController(UserSettingsService userSettingsService) {
        this.userSettingsService = userSettingsService;
    }

    @ApiOperation(value = "Changes default settings", notes = "Values to change should be provided, user will be taken from security context holder")
    @PostMapping("/update/Settings")
    public ResponseEntity<UserSettingRequest> updateUserSettings(@RequestBody UserSettingRequest request){
        userSettingsService.updateUserSettings(request);
        return ResponseEntity.ok(request);
    }
}
