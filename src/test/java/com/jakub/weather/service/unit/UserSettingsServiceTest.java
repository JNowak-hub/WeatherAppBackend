package com.jakub.weather.service.unit;

import com.jakub.weather.exceptions.UserNotFoundException;
import com.jakub.weather.exceptions.UserSettingsNotFoundException;
import com.jakub.weather.model.dto.UserSettingRequest;
import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.model.user.UserSettingsEntity;
import com.jakub.weather.repo.UserSettingsEntiyRepo;
import com.jakub.weather.service.*;
import com.jakub.weather.utils.UserSettingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserSettingsServiceTest {
    @Mock
    private UserSettingMapper settingMapper;
    @Mock
    private UserSettingsEntiyRepo settingsRepo;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserSettingsService settingsService;
    UserEntity user = new UserEntity("username", "password");

    @BeforeEach
    void setSecurityContext() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        Authentication auth = new
                UsernamePasswordAuthenticationToken(user, user.getPassword(), grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void when_deleteUserSettings_then_SettingsRepoDelete() {
        //given
        user.setId(1L);
        when(userService.findUserByUsername(user.getUsername())).thenReturn(user);
        //when
        settingsService.deleteUserSettings(user);
        //then
        verify(settingsRepo).deleteUserSettings(user.getId());
    }

    @Test
    void when_deleteUserSettings_then_throwUserNotFoundException() {
        when(userService.findUserByUsername(anyString())).thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class, () -> settingsService.deleteUserSettings(user));
    }

    @Test
    void when_getUserSettings_then_returnSettings() {
        //given
        UserSettingsEntity settings = new UserSettingsEntity();
        settings.setDefaultCity("Wroclaw");
        settings.setUser(user);
        user.setId(1L);
        when(settingsRepo.getUserSettingsByUserId(user.getId())).thenReturn(Optional.of(settings));
        //when
        UserSettingsEntity userSetting = settingsService.getUserSetting(user);
        //then
        assertThat(userSetting).isEqualTo(settings);
    }

    @Test
    void when_getUserSettings_then_throwUserSettingsNotFoundException() {
        //given
        user.setId(1L);
        when(settingsRepo.getUserSettingsByUserId(user.getId())).thenReturn(Optional.empty());
        //when
        UserSettingsNotFoundException exception = assertThrows(UserSettingsNotFoundException.class, () -> settingsService.getUserSetting(user));
        //then
        assertThat(exception.getMessage()).isEqualTo("Settings for user with id " + user.getId() + " not found");
    }

    @Test
    void when_updateUserSettings_then_updateSettings() {
        //given
        user.setId(1L);
        when(userService.findUserByUsername(user.getUsername())).thenReturn(user);
        UserSettingRequest request = new UserSettingRequest();
        //when
        settingsService.updateUserSettings(request);
        //then
        verify(settingMapper).changeSettings(request, user);
        verify(userService).updateUser(user);
    }
    @Test
    void when_updateUserSettings_then_throwUserNotFoundException(){
        //given
        UserSettingRequest request = new UserSettingRequest();
        when(userService.findUserByUsername(user.getUsername())).thenThrow(UserNotFoundException.class);
        //when /then
        assertThrows(UserNotFoundException.class, ()-> settingsService.updateUserSettings(request));
    }

}
