package com.jakub.weather.utils;

import com.jakub.weather.model.dto.UserSettingRequest;
import com.jakub.weather.model.user.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserSettingMapper {

    public UserEntity changeSettings(UserSettingRequest request, UserEntity user){
        UserEntity userEntity = user;

        if(request.getDefaultCity() != null){
            userEntity.getSettings().setDefaultCity(request.getDefaultCity());
        }
        if(request.getDaysAmount() != null){
            userEntity.getSettings().setDaysAmount(request.getDaysAmount());
        }

        return userEntity;
    }

}
