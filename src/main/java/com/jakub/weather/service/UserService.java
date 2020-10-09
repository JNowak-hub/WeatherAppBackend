package com.jakub.weather.service;

import com.jakub.weather.exceptions.UserAlreadyExists;
import com.jakub.weather.exceptions.UserNotFoundException;
import com.jakub.weather.model.weather.dto.UserSettingRequest;
import com.jakub.weather.model.weather.user.Role;
import com.jakub.weather.model.weather.user.UserEntity;
import com.jakub.weather.model.weather.user.UserSettingsEntity;
import com.jakub.weather.utils.UserSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.jakub.weather.repo.UserRepo;

import java.util.Optional;

@Service
public class UserService {

    private UserRepo userRepo;

    private UserLoggService loggService;

    private UserSettingMapper settingMapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public UserService(UserRepo userRepo, UserLoggService loggService, UserSettingMapper settingMapper) {
        this.userRepo = userRepo;
        this.loggService = loggService;
        this.settingMapper = settingMapper;
    }

    public UserEntity findById(Long id){
        return userRepo.findById(id).get();
    }

    public UserEntity createNewUser(UserEntity userEntity){

        if(userRepo.findByUsername(userEntity.getUserName()).isPresent()){
            throw new UserAlreadyExists("user " + userEntity.getUserName() + " already exists, pick other userName");
        }
        if(userEntity.getPassword().isEmpty()){
            throw new RuntimeException("Password cannot be blank");
        }

        UserSettingsEntity defaultSetting = new UserSettingsEntity();
        defaultSetting.setDaysAmount(1L);
        defaultSetting.setDefaultCity("Katowice");

        UserEntity newUser = new UserEntity(userEntity.getUserName(), encoder.encode(userEntity.getPassword()));
        newUser.getRole().add(Role.USER);
        newUser.setSettings(defaultSetting);

        userRepo.save(newUser);
        loggService.userCreated(newUser);
        return newUser;
    }

    public UserEntity findUserByUsername(String username) {
        Optional<UserEntity> potentialUser = userRepo.findByUsername(username);
        if(potentialUser.isPresent()){
            return potentialUser.get();
        }
        throw new UserNotFoundException("User : " + username + " doesn't exists" );
    }

    public void updateUserSettings(UserSettingRequest request){
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity userInDb = userRepo.getOne(currentUser.getId());
        settingMapper.changeSettings(request, userInDb);
        userRepo.save(userInDb);
    }
}
