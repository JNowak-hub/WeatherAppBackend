package com.jakub.weather.service;

import com.jakub.weather.configuration.CustomAuthorizationManager;
import com.jakub.weather.exceptions.UserAlreadyExists;
import com.jakub.weather.exceptions.UserNotFoundException;
import com.jakub.weather.exceptions.WrongInputException;
import com.jakub.weather.model.authorization.AuthorizationRequest;
import com.jakub.weather.model.dto.UserEntityRequest;
import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.model.user.UserSettingsEntity;
import com.jakub.weather.utils.UserEntityMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.jakub.weather.repo.UserRepo;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepo userRepo;

    private UserLoggService loggService;

    private RoleService roleService;

    private BCryptPasswordEncoder encoder;

    private UserEntityMapper userEntityMapper;

    public UserService(UserRepo userRepo, UserLoggService loggService, RoleService roleService, BCryptPasswordEncoder encoder, UserEntityMapper userEntityMapper) {
        this.userRepo = userRepo;
        this.loggService = loggService;
        this.roleService = roleService;
        this.encoder = encoder;
        this.userEntityMapper = userEntityMapper;
    }

    public UserEntity findById(Long id) {
        Optional<UserEntity> optionalUser = userRepo.findById(id);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("User with id: " + id + " not found");
        }
        return optionalUser.get();
    }

    @Transactional
    public UserEntity createNewUser(UserEntity userEntity) {
        validateUser(userEntity);
        if (userRepo.findByUsername(userEntity.getUserName()).isPresent()) {
            throw new UserAlreadyExists("user " + userEntity.getUserName() + " already exists, pick other userName");
        }
        UserEntity newUser = setUserDefaultSettings(userEntity);
        userRepo.save(newUser);
        loggService.userCreated(newUser);
        return newUser;
    }

    public UserEntity findUserByUsername(String username) {
        Optional<UserEntity> potentialUser = userRepo.findByUsername(username);
        if (potentialUser.isPresent()) {
            return potentialUser.get();
        }
        throw new UserNotFoundException("User : " + username + " doesn't exists");
    }

    @Transactional
    public UserEntity saveUser(UserEntity user) {
        validateUser(user);
        return userRepo.save(user);
    }

    @Transactional
    public UserEntity updateUser(UserEntityRequest userRequest) {
        UserEntity userToUpdate = findUserByUsername(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserName());
        userToUpdate = userEntityMapper.updateUser(userRequest, userToUpdate);

        return userRepo.save(userToUpdate);
    }

    @Transactional
    public void deleteUserByUserName(String username) {
        UserEntity user = findUserByUsername(username);
        userRepo.delete(user);
    }

    private UserEntity setUserDefaultSettings(UserEntity userEntity) {
        UserSettingsEntity defaultSetting = new UserSettingsEntity();
        defaultSetting.setDaysAmount(1L);
        defaultSetting.setDefaultCity("Katowice");

        UserEntity newUser = new UserEntity(userEntity.getUserName(), encoder.encode(userEntity.getPassword()));
        newUser.getRole().add(roleService.getRoleByName("USER"));
        newUser.setSettings(defaultSetting);
        return newUser;
    }

    private void validateUser(UserEntity userEntity) {
        if (userEntity == null) {
            throw new WrongInputException("User cannot be null");
        }
        if (isNotUserPasswordAndUsernameFilled(userEntity)) {
            throw new WrongInputException("userName or Password cannot be empty");
        }

    }

    private boolean isNotUserPasswordAndUsernameFilled(UserEntity userEntity) {
        return userEntity.getUserName() == null || userEntity.getPassword() == null ||
                userEntity.getPassword().isEmpty() || userEntity.getUserName().isEmpty() ||
                userEntity.getPassword().isBlank() || userEntity.getUserName().isBlank();
    }
}
