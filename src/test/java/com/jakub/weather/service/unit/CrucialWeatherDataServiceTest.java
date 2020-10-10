package com.jakub.weather.service.unit;

import com.jakub.weather.exceptions.WeatherNotFoundException;
import com.jakub.weather.model.weather.WeatherInfo;
import com.jakub.weather.model.weather.WeatherResponse;
import com.jakub.weather.model.weather.Wind;
import com.jakub.weather.model.dto.CrucialWeatherData;
import com.jakub.weather.service.CrucialWeatherDataService;
import com.jakub.weather.service.UserApiCAllHistoryService;
import com.jakub.weather.service.UserService;
import com.jakub.weather.utils.WeatherApiWebClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
public class CrucialWeatherDataServiceTest {

    private static final double WIND_DEG = 1.0;
    private static final double WIND_SPEED = 50.0;
    private static final int PRESSURE = 15;
    private static final int HUMIDITY = 22;
    private static final double TEMPERATURE = 11.0;

    @Mock
    private UserApiCAllHistoryService historyService;
    @Mock
    private WeatherApiWebClient webClientMock;
    @Mock
    private UserService userService;
    @InjectMocks
    private CrucialWeatherDataService service;

    @Test
    public void Given_CityName_When_GetWeatherInfo_Then_ReturnWeatherData() {
        //given
        WeatherResponse respone = createWeatherResponse();
        when(webClientMock.getDataFromApi(any())).thenReturn(respone);
        //when
        CrucialWeatherData data = service.getWeatherInfo("Katowice");
        //than
        assertThat(data.getHumidity()).isEqualTo(HUMIDITY);
        assertThat(data.getPressure()).isEqualTo(PRESSURE);
        assertThat(data.getTemperature()).isEqualTo(TEMPERATURE);
    }

    @Test
    public void Given_CityName_When_GetWeatherInfo_Then_Throw_WeatherNotFoundException() {
        //given
        when(webClientMock.getDataFromApi(any())).thenThrow(WeatherNotFoundException.class);
        //when
        WeatherNotFoundException myException = assertThrows(WeatherNotFoundException.class, () ->service.getWeatherInfo("Katowice"));
        //than
        assertThat(myException).isInstanceOf(WeatherNotFoundException.class);

    }

    @Test
    @WithMockUser(username="admin")
    public void Given_CityName_AND_DataType_When_getDataByType_Then_CustomString() {
        //given
        WeatherResponse respone = createWeatherResponse();
        when(webClientMock.getDataFromApi(any())).thenReturn(respone);
        //when
        String temperatureData = service.getDataByType("Katowice", "temperature");
        String humidityData = service.getDataByType("Katowice", "humidity");
        String pressureData = service.getDataByType("Katowice", "pressure");
        String windData = service.getDataByType("Katowice", "wind");
        //than
        assertThat(temperatureData).isEqualTo("Temperature in " + "Katowice" + " is " + TEMPERATURE);
        assertThat(humidityData).isEqualTo("Humidity in " + "Katowice" + " is " + HUMIDITY);
        assertThat(pressureData).isEqualTo("Pressure in " + "Katowice" + " is " + PRESSURE);
        assertThat(windData).isEqualTo("Wind in " + "Katowice" + " is " + createWeatherResponse().getWind());
    }
    @Test
    @WithMockUser(username="admin")
    public void Given_CityName_AND_DataType_When_getDataByType_Then_Throw_WeatherNotFoundException() {
        //given
        WeatherResponse respone = createWeatherResponse();
        when(webClientMock.getDataFromApi(any())).thenReturn(respone);
        //when
        WeatherNotFoundException myException = assertThrows(WeatherNotFoundException.class, () ->service.getDataByType("Katowice", "randomInput"));
        //than throws exception
        assertThat(myException.getMessage()).isEqualTo("Couldn't find data. Try to type correct information type.");
    }
    @Test
    @WithMockUser(username="admin")
    public void Given_CityName_AND_NullDataType_When_getDataByType_Then_WeatherNotFoundException() {
        //given
        WeatherResponse respone = createWeatherResponse();
        when(webClientMock.getDataFromApi(any())).thenReturn(respone);
        //when
        WeatherNotFoundException myException = assertThrows(WeatherNotFoundException.class, () ->service.getDataByType("Katowice", null));
        //than throw exception
        assertThat(myException.getMessage()).isEqualTo("input dataType cannot be null.");
    }


    private WeatherResponse createWeatherResponse() {
        WeatherResponse response = new WeatherResponse();

        Wind wind = new Wind();
        wind.setDeg(WIND_DEG);
        wind.setSpeed(WIND_SPEED);
        response.setWind(wind);

        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setPressure(PRESSURE);
        weatherInfo.setHumidity(HUMIDITY);
        weatherInfo.setTemp(TEMPERATURE);
        response.setMain(weatherInfo);

        return response;
    }
}

