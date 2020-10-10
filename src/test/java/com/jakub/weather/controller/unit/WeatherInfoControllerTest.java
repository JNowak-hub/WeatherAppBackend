package com.jakub.weather.controller.unit;

import com.jakub.weather.configuration.CustomExceptionHandler;
import com.jakub.weather.controller.WeatherInfoController;
import com.jakub.weather.exceptions.UserNotFoundException;
import com.jakub.weather.exceptions.WeatherNotFoundException;
import com.jakub.weather.exceptions.WrongInputException;
import com.jakub.weather.model.dto.CrucialWeatherData;
import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.model.user.UserSettingsEntity;
import com.jakub.weather.model.weather.Wind;
import com.jakub.weather.service.CrucialWeatherDataService;
import com.jakub.weather.service.UserSettingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
public class WeatherInfoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CrucialWeatherDataService service;
    @Mock
    private UserSettingsService settingsService;
    @InjectMocks
    private WeatherInfoController controller;
    private UserEntity user;
    private UserSettingsEntity settings;

    @BeforeEach
    void mockSetup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();
        user = new UserEntity("username", "password");
        settings = new UserSettingsEntity();
        settings.setDefaultCity("Katowice");
        user.setSettings(settings);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        Authentication auth = new
                UsernamePasswordAuthenticationToken(user, user.getPassword(), grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void when_getDataByType_returnStatusOk() throws Exception {
        String cityName = "Katowice";
        String dataType = "temperature";
        double temperature = 20.0;
        String response = "Temperature in " + cityName + " is " + temperature;
        when(service.getDataByType(cityName, dataType)).thenReturn(response);
        mockMvc.perform(get("/api/weather/data")
                .param("cityName", cityName)
                .param("dataType", dataType))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(response));
    }

    @Test
    void when_getDataByType_throwUserNotFoundException() throws Exception {
        String cityName = "Katowice";
        String dataType = "temperature";
        when(service.getDataByType(cityName, dataType)).thenThrow(UserNotFoundException.class);
        mockMvc.perform(get("/api/weather/data")
                .param("cityName", cityName)
                .param("dataType", dataType))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void when_getDataByType_throwWeatherNotFoundException() throws Exception {
        String cityName = "Katowice";
        String dataType = "temperature";
        when(service.getDataByType(cityName, dataType)).thenThrow(WeatherNotFoundException.class);
        mockMvc.perform(get("/api/weather/data")
                .param("cityName", cityName)
                .param("dataType", dataType))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void when_getDefaultWeatherData_returnCrucialWeatherData() throws Exception {
        CrucialWeatherData data = new CrucialWeatherData();
        Wind wind = new Wind();
        wind.setDeg(9.0);
        wind.setSpeed(200.0);
        data.setHumidity(23);
        data.setPressure(30);
        data.setTemperature(29.0);
        data.setWindInfo(wind);
        when(service.getWeatherInfo(any())).thenReturn(data);
        when(settingsService.getUserSetting(user)).thenReturn(settings);
        mockMvc.perform(get("/api/weather/data/default"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.humidity", is(data.getHumidity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pressure", is(data.getPressure())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.temperature", is(data.getTemperature())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.windInfo.deg", is(data.getWindInfo().getDeg())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.windInfo.speed", is(data.getWindInfo().getSpeed())));
    }
    @Test
    void when_getDefaultWeatherData_throwWrongInputException() throws Exception {
        when(service.getWeatherInfo(any())).thenThrow(WrongInputException.class);
        when(settingsService.getUserSetting(user)).thenReturn(settings);
        mockMvc.perform(get("/api/weather/data/default"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void when_getDefaultWeatherData_throwWeatherNotFoundException() throws Exception {
        when(service.getWeatherInfo(any())).thenThrow(WeatherNotFoundException.class);
        when(settingsService.getUserSetting(user)).thenReturn(settings);
        mockMvc.perform(get("/api/weather/data/default"))
                .andExpect(status().isNotFound());
    }
}
