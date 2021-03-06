package com.jakub.weather.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserSettingsNotFoundException extends RuntimeException{

    public UserSettingsNotFoundException(String message) {
        super(message);
    }

}
