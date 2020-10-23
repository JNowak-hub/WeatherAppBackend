package com.jakub.weather.controller.unit;

import com.jakub.weather.configuration.CustomExceptionHandler;
import com.jakub.weather.controller.AuthVerifyController;
import com.jakub.weather.model.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
public class AuthVerifyControllerTest {

    private AuthVerifyController controller = new AuthVerifyController();

    private MockMvc mockMvc;

    private UserEntity user;

    @BeforeEach
    void mockSetup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();

        user = new UserEntity("user","password");

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        Authentication auth = new
                UsernamePasswordAuthenticationToken(user, user.getPassword(), grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }


    @Test
    public void when_verifyAuth_returnTrue() throws Exception {
        mockMvc.perform(get("/api/verify/context"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));

    }

    @Test
    public void when_verifyAuth_returnFalse() throws Exception {
        SecurityContextHolder.clearContext();
        mockMvc.perform(get("/api/verify/context"))
                .andExpect(status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string("false"));

    }
}
