package com.jakub.weather.service;

import com.jakub.weather.exceptions.WeatherNotFoundException;
import com.jakub.weather.model.weather.WeatherResponse;
import com.jakub.weather.model.weather.Wind;
import com.jakub.weather.model.dto.CrucialWeatherData;
import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.utils.WeatherApiWebClient;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CrucialWeatherDataService {


    private WeatherApiWebClient webClient;

    private UserApiCAllHistoryService historyService;

    private UserService userService;

    public CrucialWeatherDataService(WeatherApiWebClient webClient, UserApiCAllHistoryService historyService, UserService userService) {
        this.webClient = webClient;
        this.historyService = historyService;
        this.userService = userService;
    }


    public String getDataByType(String cityName, String dataType) {
        String currentUsername = ((UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).getUsername();
        UserEntity user = userService.findUserByUsername(currentUsername);
        return getDataByTypeAndSaveApiCallHistory(cityName, dataType, user);
    }

    private String getDataByTypeAndSaveApiCallHistory(String cityName, String dataType, UserEntity user) {
        if (dataType == null) {
            throw new WeatherNotFoundException("input dataType cannot be null.");
        }
        switch (dataType){
            case "humidity":
                historyService.callApi("Humidity in " + cityName + " is " + getHumidity(cityName), user);
                return "Humidity in " + cityName + " is " + getHumidity(cityName);
            case "pressure":
                historyService.callApi("Pressure in " + cityName + " is " + getPressure(cityName), user);
                return "Pressure in " + cityName + " is " + getPressure(cityName);
            case "wind":
                historyService.callApi("Wind in " + cityName + " is " + getWindInformation(cityName), user);
                return "Wind in " + cityName + " is " + getWindInformation(cityName);
            case "temperature":
                historyService.callApi("Temperature in " + cityName + " is " + getTemperature(cityName), user);
                return "Temperature in " + cityName + " is " + getTemperature(cityName);
            default:
                throw new WeatherNotFoundException("Couldn't find data. Try to type correct information type.");
        }
    }


    public CrucialWeatherData getWeatherInfo(String cityName) {

        WeatherResponse weatherData = webClient.getDataFromApi(cityName);

        CrucialWeatherData weatherInfo = new CrucialWeatherData();
        weatherInfo.setHumidity(weatherData.getMain().getHumidity());
        weatherInfo.setPressure(weatherData.getMain().getPressure());
        weatherInfo.setTemperature(weatherData.getMain().getTemp());
        weatherInfo.setWindInfo(weatherData.getWind());

        return weatherInfo;
    }

    private Double getTemperature(String cityName) {
        return getWeatherInfo(cityName).getTemperature();
    }

    private Integer getPressure(String cityName) {
        return getWeatherInfo(cityName).getPressure();
    }

    private Integer getHumidity(String cityName) {
        return getWeatherInfo(cityName).getHumidity();
    }

    private Wind getWindInformation(String cityName) {
        return getWeatherInfo(cityName).getWindInfo();
    }

}
