package com.jakub.weather;

import com.jakub.weather.model.weather.WeatherResponse;
import com.jakub.weather.utils.WeatherApiWebClient;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class WeatherApiConnectorTest {

    @Autowired
    private WeatherApiWebClient connector;

    @Test
    void mapInfoToClass() throws IOException, InterruptedException {
        WeatherResponse response = null;
        response = connector.getDataFromApi("Katowice").get();
        Assert.assertNotNull(response);
    }
}
