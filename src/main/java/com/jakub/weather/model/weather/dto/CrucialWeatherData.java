package com.jakub.weather.model.weather.dto;

import com.jakub.weather.model.weather.Wind;

public class CrucialWeatherData {

    private Double temperature;
    private Integer humidity;
    private Integer pressure;
    private Wind windInfo;


    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Wind getWindInfo() {
        return windInfo;
    }

    public void setWindInfo(Wind windInfo) {
        this.windInfo = windInfo;
    }
}
