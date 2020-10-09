package com.jakub.weather.service;

import com.jakub.weather.model.weather.user.UserApiCallHistoryEntity;
import com.jakub.weather.model.weather.user.UserEntity;
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
        UserApiCallHistoryEntity historyElement = new UserApiCallHistoryEntity();
        historyElement.setTime(LocalDateTime.now());
        historyElement.setUserEntity(user);
        historyElement.setData(data);
        historyRepo.save(historyElement);

    }
}
