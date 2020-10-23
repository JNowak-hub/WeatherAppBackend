package com.jakub.weather.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/verify")
public class AuthVerifyController {

    @ApiOperation(value = "Returns true when user is authenticated and false if not", notes = "Should be used on front-end side for redirecting when unauthorized")
    @GetMapping("/context")
    public ResponseEntity<Boolean> verifyAuth(){
        if(SecurityContextHolder.getContext() == null || SecurityContextHolder.getContext().getAuthentication() == null || !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(false);
        }
        return ResponseEntity.ok(true);
    }
}
