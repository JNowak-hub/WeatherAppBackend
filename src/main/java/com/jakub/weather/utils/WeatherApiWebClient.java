package com.jakub.weather.utils;

import com.jakub.weather.model.weather.WeatherResponse;
import com.jakub.weather.model.weather.user.UserEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.Optional;

@Component
public class WeatherApiWebClient {

    public Optional<WeatherResponse> getDataFromApi(String cityName){

        WebClient.RequestBodySpec request = WebClient.create()
                .method(HttpMethod.POST)
                .uri(URI.create("http://api.openweathermap.org/data/2.5/weather?q="+ cityName +"&appid=8af1f753ac7c4d67cd7987b1c374e618"));

        return Optional.ofNullable(request.exchange().block().bodyToMono(WeatherResponse.class).block());
    }
}
