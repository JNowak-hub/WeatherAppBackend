package com.jakub.weather.controller;

import com.jakub.weather.model.dto.UserSettingRequest;
import com.jakub.weather.service.UserSettingsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin({"http://localhost:3000","http://localhost:8080","http://localhost:5000"})
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
