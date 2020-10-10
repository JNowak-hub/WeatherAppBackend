package com.jakub.weather.service;

import com.jakub.weather.model.user.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Service
public class LogOutSucessHandler extends SimpleUrlLogoutSuccessHandler {

    private UserLoggService loggService;

    public LogOutSucessHandler(UserLoggService loggService) {
        this.loggService = loggService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserEntity user = (UserEntity) authentication.getPrincipal();

        loggService.logLogout(user);

        super.onLogoutSuccess(request, response, authentication);
    }
}
