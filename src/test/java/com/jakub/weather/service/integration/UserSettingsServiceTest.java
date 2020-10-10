package com.jakub.weather.service.integration;

import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.model.user.UserSettingsEntity;
import com.jakub.weather.service.UserApiCAllHistoryService;
import com.jakub.weather.service.UserService;
import com.jakub.weather.service.UserSettingsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserSettingsServiceTest {

    @Autowired
    private UserSettingsService settingsService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserApiCAllHistoryService cAllHistoryService;

    private UserSettingsEntity userSettingsEntity;

    private UserEntity user = new UserEntity();
    private final String USER_PASSWORD = "password";
    private final String USERNAME = "Testusername";

    @BeforeEach
    void init() {
        user.setPassword(USER_PASSWORD);
        user.setUserName(USERNAME);
        userService.createNewUser(user);
    }
    @AfterEach
    void cleanUp(){
        userService.deleteUserByUserName(user.getUserName());
    }

    @Test
    public void Given_User_When_getUserSetting_Than_return_settings() {
        //given
        userSettingsEntity = new UserSettingsEntity();
        userSettingsEntity.setDefaultCity("Katowice");
        userSettingsEntity.setDaysAmount(3L);
        UserEntity user = userService.findUserByUsername(USERNAME);
        //when
        UserSettingsEntity userSettings = settingsService.getUserSetting(user);
        //than
        assertThat(userSettings.getDefaultCity()).isEqualTo("Katowice");
    }
}
