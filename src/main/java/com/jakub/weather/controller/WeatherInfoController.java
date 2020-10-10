package com.jakub.weather.controller;

import com.jakub.weather.model.dto.CrucialWeatherData;
import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.service.CrucialWeatherDataService;
import com.jakub.weather.service.UserSettingsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherInfoController {

    private CrucialWeatherDataService service;
    private UserSettingsService settingsService;

    public WeatherInfoController(CrucialWeatherDataService service, UserSettingsService settingsService) {
        this.service = service;
        this.settingsService = settingsService;
    }
    @ApiOperation(value = "Return specific weather information based on dataType", notes = "First parameter is city and second is dataType choose one of following: temperature, humidity, wind, pressure")
    @GetMapping("/data")
    public ResponseEntity<String> getDataByType(@RequestParam String cityName, @RequestParam String dataType){
            return ResponseEntity.ok(service.getDataByType(cityName, dataType));
    }

    @ApiOperation(value = "Returns full data collected Via external weather info api", notes = "values will come as JSON format")
    @GetMapping("/data/default")
    public ResponseEntity<CrucialWeatherData> getDefaultWeatherData(){
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(service.getWeatherInfo(settingsService.getUserSetting(user).getDefaultCity()));
    }

}
