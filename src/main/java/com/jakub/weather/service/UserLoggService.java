package com.jakub.weather.service;

import com.jakub.weather.exceptions.UserNotFoundException;
import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.model.user.UserLoggEntity;
import com.jakub.weather.repo.UserLoggEntityRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserLoggService {

    private UserLoggEntityRepo repo;

    public UserLoggService(UserLoggEntityRepo repo) {
        this.repo = repo;
    }

    public void loggLogin(UserEntity user){
        if(user == null){
            throw new UserNotFoundException("User cannot be null");
        }
        UserLoggEntity logg = new UserLoggEntity();
        logg.setMessage("User Logged In");
        logg.setTime(LocalDateTime.now());
        logg.setUser(user);
        repo.save(logg);
    }

    public void logLogout(UserEntity user){
        if(user == null){
            throw new UserNotFoundException("User cannot be null");
        }
        UserLoggEntity logg = new UserLoggEntity();
        logg.setMessage("User LogOut");
        logg.setTime(LocalDateTime.now());
        logg.setUser(user);
        repo.save(logg);
    }

    public void userCreated(UserEntity user){
        if(user == null){
            throw new UserNotFoundException("User cannot be null");
        }
        UserLoggEntity logg = new UserLoggEntity();
        logg.setMessage("User registered");
        logg.setTime(LocalDateTime.now());
        logg.setUser(user);
        repo.save(logg);
    }
}