package com.jakub.weather.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class UserSettingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String defaultCity;

    @Column
    private Long daysAmount;

    @OneToOne(cascade = CascadeType.ALL, targetEntity = UserEntity.class, mappedBy = "settings", fetch = FetchType.LAZY)
    private UserEntity user;

    @JsonIgnore
    public UserEntity getUser() {
        return user;
    }

    @JsonProperty
    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getDefaultCity() {
        return defaultCity;
    }

    public void setDefaultCity(String defaultCity) {
        this.defaultCity = defaultCity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDaysAmount() {
        return daysAmount;
    }

    public void setDaysAmount(Long daysAmount) {
        this.daysAmount = daysAmount;
    }
}
