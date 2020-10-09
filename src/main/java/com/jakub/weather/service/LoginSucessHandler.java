package com.jakub.weather.service;

import com.jakub.weather.model.weather.user.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class LoginSucessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private UserLoggService loggService;

    public LoginSucessHandler(UserLoggService loggService) {
        this.loggService = loggService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        loggService.loggLogin(user);
        super.onAuthenticationSuccess(request, response, authentication);
    }


}
