package com.jakub.weather.controller;

import com.jakub.weather.model.authorization.AuthorizationRequest;
import com.jakub.weather.model.dto.UserEntityRequest;
import com.jakub.weather.model.dto.UserSettingRequest;
import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.service.LoginService;
import com.jakub.weather.service.UserService;
import com.jakub.weather.service.UserSettingsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserController {

    private UserSettingsService userSettingsService;
    private UserService userService;

    public UserController(UserSettingsService userSettingsService, UserService userService) {
        this.userSettingsService = userSettingsService;
        this.userService = userService;
    }

    @ApiOperation(value = "Changes default settings", notes = "Values to change should be provided, user will be taken from security context holder")
    @PostMapping("/update/Settings")
    public ResponseEntity<UserSettingRequest> updateUserSettings(@RequestBody UserSettingRequest request){
        userSettingsService.updateUserSettings(request);
        return ResponseEntity.ok(request);
    }

    @ApiOperation(value = "Gets current user details", notes = "Returns user details without password")
    @GetMapping("/get/user")
    public ResponseEntity<UserEntity> getCurrentUser(){
        String userName = ((UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserName();
        return ResponseEntity.ok(userService.findUserByUsername(userName));
    }

    @ApiOperation(value = "Update User info", notes = "Update user fields userName, password, one or two can be updated")
    @PostMapping("/update/user")
    public ResponseEntity<String> updateUser(@RequestBody UserEntityRequest userRequest){
        UserEntity updatedUser = userService.updateUser(userRequest);

        setSecurityContextHolder(updatedUser);

        return ResponseEntity.ok("User updated");
    }

    private void setSecurityContextHolder(UserEntity updatedUser) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(updatedUser.getRole().toString()));
        Authentication auth = new
                UsernamePasswordAuthenticationToken(updatedUser, updatedUser.getPassword(), grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
