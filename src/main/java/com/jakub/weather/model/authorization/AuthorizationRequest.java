package com.jakub.weather.model.authorization;

import java.io.Serializable;

public class AuthorizationRequest implements Serializable {

    private String userName;
    private String password;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
