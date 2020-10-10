package com.jakub.weather.service;

import com.jakub.weather.exceptions.UserSettingsNotFoundException;
import com.jakub.weather.model.dto.UserSettingRequest;
import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.model.user.UserSettingsEntity;
import com.jakub.weather.repo.UserSettingsEntiyRepo;
import com.jakub.weather.utils.UserSettingMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserSettingsService {

    private UserSettingMapper settingMapper;

    private UserSettingsEntiyRepo settingsRepo;

    private UserService userService;


    public UserSettingsService(UserSettingMapper settingMapper, UserSettingsEntiyRepo settingsRepo, UserService userService) {
        this.settingMapper = settingMapper;
        this.settingsRepo = settingsRepo;
        this.userService = userService;
    }

    public void deleteUserSettings(UserEntity user){
        UserEntity foundUser = userService.findUserByUsername(user.getUsername());
        settingsRepo.deleteUserSettings(foundUser.getId());
    }

    public UserSettingsEntity getUserSetting(UserEntity user){
        Optional<UserSettingsEntity> optionalSettings = settingsRepo.getUserSettingsByUserId(user.getId());
        if(optionalSettings.isEmpty()){
            throw new UserSettingsNotFoundException("Settings for user with id " + user.getId() + " not found");
        }
        return optionalSettings.get();
    }

    @Transactional
    public void updateUserSettings(UserSettingRequest request){
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity userInDb = userService.findUserByUsername(currentUser.getUsername());
        settingMapper.changeSettings(request, userInDb);
        userService.updateUser(userInDb);
    }

}
