package com.jakub.weather.utils;

import com.jakub.weather.model.dto.UserEntityRequest;
import com.jakub.weather.model.user.UserEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserEntityMapper {


    private BCryptPasswordEncoder encoder;

    public UserEntityMapper(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public UserEntity updateUser(UserEntityRequest userEntityRequest, UserEntity userEntity) {
        if (userEntityRequest.getUserName() != null) {
            userEntity.setUserName(userEntityRequest.getUserName());
        }
        if (userEntityRequest.getPassword() != null) {
            userEntity.setPassword(encoder.encode(userEntityRequest.getPassword()));
        }
        return userEntity;
    }
}
