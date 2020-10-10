package com.jakub.weather.service.integration;

import com.jakub.weather.exceptions.WrongInputException;
import com.jakub.weather.model.weather.WeatherResponse;
import com.jakub.weather.utils.WeatherApiWebClient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class WeatherApiConnectorTest {

    @Autowired
    private WeatherApiWebClient connector;

    @Test
    public void Given_City_When_getDataFromApi_Than_return_notNull_response() throws IOException, InterruptedException {
        //given
        WeatherResponse response = null;
        //when
        response = connector.getDataFromApi("Katowice");
        //than
        assertThat(response).isNotNull();
    }

    @Test
    public void Given_Blank_CityName_When_getDataFormApi_Than_Throw_WrongInputException() {
        //given
        String cityName = "   ";
        //when
        WrongInputException myException = assertThrows(WrongInputException.class, () -> connector.getDataFromApi(cityName));
        //than throws exception
        assertThat(myException.getMessage()).isEqualTo("City name cannot be empty or start with blank characters!");
    }

    @Test
    public void Given_Empty_CityName_When_getDataFormApi_Than_Throw_WrongInputException() {
        //given
        String cityName = "";
        //when
        WrongInputException myException = assertThrows(WrongInputException.class, () -> connector.getDataFromApi(cityName));
        //than throws exception
        assertThat(myException.getMessage()).isEqualTo("City name cannot be empty or start with blank characters!");
    }

    @Test
    public void Given_Null_CityName_When_getDataFormApi_Than_Throw_WrongInputException() {
        //given
        String cityName = null;
        //when
        WrongInputException myException = assertThrows(WrongInputException.class, () -> connector.getDataFromApi(cityName));
        //than throws exception
        assertThat(myException.getMessage()).isEqualTo("City name cannot be empty or start with blank characters!");
    }

}
