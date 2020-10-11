package com.jakub.weather.utils.unit;

import com.jakub.weather.exceptions.UserNotFoundException;
import com.jakub.weather.exceptions.UserSettingsNotFoundException;
import com.jakub.weather.exceptions.WeatherNotFoundException;
import com.jakub.weather.exceptions.WrongInputException;
import com.jakub.weather.model.dto.UserSettingRequest;
import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.model.user.UserSettingsEntity;
import com.jakub.weather.model.weather.Clouds;
import com.jakub.weather.model.weather.WeatherResponse;
import com.jakub.weather.repo.UserSettingsEntiyRepo;
import com.jakub.weather.service.*;
import com.jakub.weather.utils.UserSettingMapper;
import com.jakub.weather.utils.WeatherApiWebClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class WeatherApiWebClientTest {

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    @InjectMocks
    private WeatherApiWebClient apiWebClient;

    private WeatherResponse response;

    @Mock
    private Mono<ClientResponse> responseMono;
    @Mock
    private ClientResponse clientResponse;
    @Mock
    private Mono<WeatherResponse> weatherResponseMono;


    @Test
    void when_getDataFromApi_then_throwWeatherNotFoundException(){
        //given
        WeatherResponse nullResponse = null;
        setupMockChain();
        when(weatherResponseMono.block()).thenReturn(nullResponse);
        //when
        WeatherNotFoundException exception = assertThrows(WeatherNotFoundException.class, () -> apiWebClient.getDataFromApi("Katowice"));
        //then
        assertThat(exception.getMessage()).isEqualTo("Couldn't get weather from external server");
    }

    @Test
    void when_getDataFromApi_then_returnWeatherResponse(){
        //given
        createWeatherResponse();
        setupMockChain();
        when(weatherResponseMono.block()).thenReturn(response);
        //when
        WeatherResponse createdResponse = apiWebClient.getDataFromApi("Katowice");
        //then
        assertThat(createdResponse.getId()).isEqualTo(1);
        assertThat(createdResponse.getBase()).isEqualTo("test base");
        assertThat(createdResponse.getClouds()).isNotNull();
        assertThat(createdResponse.getCod()).isEqualTo(2);
        assertThat(createdResponse.getDt()).isEqualTo(3L);
        assertThat(createdResponse.getName()).isEqualTo("test name");
    }

    @Test
    void when_getDataFromApiNull_then_throwWrongInputException(){
        //given
        String cityName = null;
        //when
        WrongInputException exception = assertThrows(WrongInputException.class, () -> apiWebClient.getDataFromApi(cityName));
        //then
        assertThat(exception.getMessage()).isEqualTo("City name cannot be empty or start with blank characters!");
    }

    @Test
    void when_getDataFromApiEmpty_then_throwWrongInputException(){
        //given
        String cityName = "";
        //when
        WrongInputException exception = assertThrows(WrongInputException.class, () -> apiWebClient.getDataFromApi(cityName));
        //then
        assertThat(exception.getMessage()).isEqualTo("City name cannot be empty or start with blank characters!");
    }
    @Test
    void when_getDataFromApiBlank_then_throwWrongInputException(){
        //given
        String cityName = "  ";
        //when
        WrongInputException exception = assertThrows(WrongInputException.class, () -> apiWebClient.getDataFromApi(cityName));
        //then
        assertThat(exception.getMessage()).isEqualTo("City name cannot be empty or start with blank characters!");
    }

    private void setupMockChain() {
        when(webClient.method(any())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(URI.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.exchange()).thenReturn(responseMono);
        when(responseMono.block()).thenReturn(clientResponse);
        when(clientResponse.bodyToMono(eq(WeatherResponse.class))).thenReturn(weatherResponseMono);
    }

    private WeatherResponse createWeatherResponse(){
        response = new WeatherResponse();
        response.setBase("test base");
        response.setClouds(new Clouds());
        response.setCod(2);
        response.setDt(3L);
        response.setId(1);
        response.setName("test name");
        response.setVisibility(100L);
        return response;
    }

}
