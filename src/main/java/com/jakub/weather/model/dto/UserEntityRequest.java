package com.jakub.weather.model.dto;

import com.jakub.weather.model.user.Role;
import java.util.ArrayList;
import java.util.List;

public class UserEntityRequest {

    private String userName;

    private String password;

    private List<Role> role = new ArrayList<>();


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }

}
