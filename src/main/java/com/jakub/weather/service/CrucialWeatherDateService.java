package com.jakub.weather.service;

import com.jakub.weather.exceptions.WeatherNotFoundException;
import com.jakub.weather.model.weather.WeatherResponse;
import com.jakub.weather.model.weather.Wind;
import com.jakub.weather.model.weather.dto.CrucialWeatherData;
import com.jakub.weather.model.weather.user.UserEntity;
import com.jakub.weather.utils.WeatherApiWebClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CrucialWeatherDateService {


    private WeatherApiWebClient webClient;

    private UserApiCAllHistoryService historyService;

    private UserService userService;

    public CrucialWeatherDateService(WeatherApiWebClient webClient, UserApiCAllHistoryService historyService, UserService userService) {
        this.webClient = webClient;
        this.historyService = historyService;
        this.userService = userService;
    }

    public Double getTemperature(String cityName){
        CrucialWeatherData data = getWeatherInfo(cityName);
        return data.getTemperature();
    }
    public Integer getPressure(String cityName){
        CrucialWeatherData data = getWeatherInfo(cityName);
        return data.getPressure();
    }
    public Integer getHumidity(String cityName){
        CrucialWeatherData data = getWeatherInfo(cityName);
        return data.getHumidity();
    }

    public Wind getWindInformation(String cityName){
        CrucialWeatherData data = getWeatherInfo(cityName);
        return data.getWindInfo();
    }

    public String getDataByType(String cityName, String dataType){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userPrincipal = (UserEntity) authentication.getPrincipal();

        UserEntity user = userService.findUserByUsername(userPrincipal.getUserName());

        if(dataType.equals("temperature")){
            historyService.callApi("Temperature in " + cityName + " is " + getTemperature(cityName), user);
            return "Temperature in " + cityName + " is " + getTemperature(cityName);
        } else if(dataType.equals("humidity")){
            historyService.callApi("Humidity in " + cityName + " is " + getHumidity(cityName), user);
            return "Humidity in " + cityName + " is " + getHumidity(cityName);
        } else if(dataType.equals("pressure")){
            historyService.callApi("Pressure in " + cityName + " is " + getPressure(cityName), user);
            return "Pressure in " + cityName + " is " + getPressure(cityName);
        }
        else if(dataType.equals("wind")){
            historyService.callApi("Wind in " + cityName + " is " + getWindInformation(cityName), user);
            return "Wind in " + cityName + " is " + getWindInformation(cityName);
        } else
            throw new WeatherNotFoundException("Couldn't find data. Try to type correct input.");
    }


    public CrucialWeatherData getWeatherInfo(String cityName){

        Optional<WeatherResponse> optionalCrucialWeatherData = webClient.getDataFromApi(cityName);
        if(optionalCrucialWeatherData.isEmpty()){
            throw new WeatherNotFoundException("Couldn't load weather information");
        }
        WeatherResponse response = optionalCrucialWeatherData.get();
        CrucialWeatherData data = new CrucialWeatherData();
        data.setHumidity(response.getMain().getHumidity());
        data.setPressure(response.getMain().getPressure());
        data.setTemperature(response.getMain().getTemp());
        data.setWindInfo(response.getWind());

        return data;
    }

}
