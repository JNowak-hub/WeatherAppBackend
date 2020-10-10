package com.jakub.weather.utils.unit;

import com.jakub.weather.model.dto.UserSettingRequest;
import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.model.user.UserSettingsEntity;
import com.jakub.weather.utils.UserSettingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserSettingMapperTest {

    private UserSettingRequest request;
    private UserEntity user;
    private UserSettingsEntity settings;
    private UserSettingMapper mapper;

    @BeforeEach
    void setUp(){
        mapper = new UserSettingMapper();
        request = new UserSettingRequest();
        user = new UserEntity();
        settings = new UserSettingsEntity();
        user.setSettings(settings);
    }

    @Test
    void when_changeSettings_changeDefaultCity(){
        //given
        request.setDefaultCity("Katowice");
        //when
        user = mapper.changeSettings(request, user);
        //than
        assertThat(user.getSettings().getDefaultCity()).isEqualTo(request.getDefaultCity());
    }
    @Test
    void when_changeSettings_changeDefaultDaysAmount(){
        //given
        request.setDaysAmount(2L);
        //when
        user = mapper.changeSettings(request,user);
        //than
        assertThat(user.getSettings().getDaysAmount()).isEqualTo(request.getDaysAmount());
    }
    @Test
    void when_changeSettings_changeDefaultDaysAmountAndDefaultCity(){
        //given
        request.setDaysAmount(2L);
        request.setDefaultCity("Katowice");
        //when
        user = mapper.changeSettings(request,user);
        //than
        assertThat(user.getSettings().getDaysAmount()).isEqualTo(request.getDaysAmount());
        assertThat(user.getSettings().getDefaultCity()).isEqualTo(request.getDefaultCity());
    }
    @Test
    void when_changeSettings_doNotChangeSettings(){
        //given
        String city = "Katowice";
        Long days = 3L;
        user.getSettings().setDefaultCity(city);
        user.getSettings().setDaysAmount(days);
        //when
        user = mapper.changeSettings(request,user);
        //than
        assertThat(user.getSettings().getDaysAmount()).isEqualTo(days);
        assertThat(user.getSettings().getDefaultCity()).isEqualTo(city);
    }
}
