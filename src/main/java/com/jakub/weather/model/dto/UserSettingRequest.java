package com.jakub.weather.model.dto;

public class UserSettingRequest {

    private String defaultCity;

    private Long daysAmount;

    public String getDefaultCity() {
        return defaultCity;
    }

    public void setDefaultCity(String defaultCity) {
        this.defaultCity = defaultCity;
    }

    public Long getDaysAmount() {
        return daysAmount;
    }

    public void setDaysAmount(Long daysAmount) {
        this.daysAmount = daysAmount;
    }
}
