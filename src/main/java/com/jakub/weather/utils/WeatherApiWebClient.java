package com.jakub.weather.utils;

import com.jakub.weather.exceptions.WeatherNotFoundException;
import com.jakub.weather.exceptions.WrongInputException;
import com.jakub.weather.model.weather.WeatherResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.Optional;

@Component
public class WeatherApiWebClient {

    public WeatherResponse getDataFromApi(String cityName) {

        if (cityName == null || cityName.isBlank() || cityName.isEmpty()) {
            throw new WrongInputException("City name cannot be empty or start with blank characters!");
        }
        WebClient.RequestBodySpec request = WebClient.create()
                .method(HttpMethod.POST)
                .uri(URI.create("http://api.openweathermap.org/data/2.5/weather?q=" + cityName.trim() + "&appid=8af1f753ac7c4d67cd7987b1c374e618"));
        Optional<WeatherResponse> response = Optional.ofNullable(request.exchange().block().bodyToMono(WeatherResponse.class).block());
        if (response.isEmpty()) {
            throw new WeatherNotFoundException("Couldn't get weather from external server");
        }
        return response.get();
    }
}
