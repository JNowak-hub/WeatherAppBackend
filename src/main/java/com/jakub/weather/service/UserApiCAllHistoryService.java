package com.jakub.weather.service;

import com.jakub.weather.exceptions.WrongInputException;
import com.jakub.weather.model.user.UserApiCallHistoryEntity;
import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.repo.UserApiCallHistoryRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserApiCAllHistoryService {

    private UserApiCallHistoryRepo historyRepo;

    public UserApiCAllHistoryService(UserApiCallHistoryRepo historyRepo) {
        this.historyRepo = historyRepo;
    }

    public void callApi(String data, UserEntity user){
        validateInputData(data, user);
        UserApiCallHistoryEntity historyElement = new UserApiCallHistoryEntity();
        historyElement.setTime(LocalDateTime.now());
        historyElement.setUserEntity(user);
        historyElement.setData(data);
        historyRepo.save(historyElement);

    }

    private void validateInputData(String data, UserEntity user) {
        if(user == null){
            throw new WrongInputException("User cannot be null");
        }
        if (data == null || data.isBlank() || data.isEmpty()) {
            throw new WrongInputException("data cannot be empty or blank");
        }
    }
}
