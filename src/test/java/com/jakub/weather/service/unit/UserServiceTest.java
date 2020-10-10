package com.jakub.weather.service.unit;

import com.jakub.weather.exceptions.UserAlreadyExists;
import com.jakub.weather.exceptions.UserNotFoundException;
import com.jakub.weather.exceptions.WrongInputException;
import com.jakub.weather.model.user.Role;
import com.jakub.weather.model.user.UserEntity;
import com.jakub.weather.repo.UserRepo;
import com.jakub.weather.service.RoleService;
import com.jakub.weather.service.UserLoggService;
import com.jakub.weather.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    private UserEntity user;
    @Mock
    private UserRepo userRepo;
    @Mock
    private UserLoggService loggService;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private RoleService roleService;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void userSetup(){
        Role role = new Role();
        role.setRole("USER");
        user = new UserEntity("username","password");
        when(passwordEncoder.encode(anyString())).thenReturn(user.getPassword());
        when(roleService.getRoleByName("USER")).thenReturn(role);
    }

    @Test
    void when_createNewUser_then_userRepoSave() {
        when(userRepo.save(any())).thenReturn(user);
        //when
        UserEntity newUser = userService.createNewUser(user);
        //then
        verify(userRepo).save(newUser);
        verify(passwordEncoder).encode(user.getPassword());
        verify(roleService).getRoleByName("USER");
        verify(loggService).userCreated(newUser);
        assertThat(newUser.getUserName()).isEqualTo("username");
        assertThat(newUser.getRole().get(0).getRole()).isEqualTo("USER");
    }

    @Test
    void when_createNewNullUser_then_WrongInputException() {
        //given
        user = null;
        when(userRepo.save(user)).thenReturn(null);
        //when
        WrongInputException exception = assertThrows(WrongInputException.class, () -> userService.createNewUser(user));
        //then
        assertThat(exception.getMessage()).isEqualTo("User cannot be null");
    }

    @Test
    void when_createNewUserWithoutUserName_then_WrongInputException() {
        //given
        user.setUserName("");
        //when
        WrongInputException exception = assertThrows(WrongInputException.class, () -> userService.createNewUser(user));
        //then
        assertThat(exception.getMessage()).isEqualTo("userName or Password cannot be empty");
    }

    @Test
    void when_createNewUserWithoutPassword_then_WrongInputException() {
        //given
        user.setPassword("");
        //when
        WrongInputException exception = assertThrows(WrongInputException.class, () -> userService.createNewUser(user));
        //then
        assertThat(exception.getMessage()).isEqualTo("userName or Password cannot be empty");
    }

    @Test
    void when_createNewUserWithNullPassword_then_WrongInputException() {
        //given
        user.setPassword(null);
        //when
        WrongInputException exception = assertThrows(WrongInputException.class, () -> userService.createNewUser(user));
        //then
        assertThat(exception.getMessage()).isEqualTo("userName or Password cannot be empty");
    }

    @Test
    void when_createNewUserWithNullUserName_then_WrongInputException() {
        //given
        user.setUserName(null);
        //when
        WrongInputException exception = assertThrows(WrongInputException.class, () -> userService.createNewUser(user));
        //then
        assertThat(exception.getMessage()).isEqualTo("userName or Password cannot be empty");
    }

    @Test
    void when_createNewUserAlreadyInDb_then_UserAlreadyExists() {
        //given
        when(userRepo.findByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));
        //when
        UserAlreadyExists exception = assertThrows(UserAlreadyExists.class, () -> userService.createNewUser(user));
        //then
        assertThat(exception.getMessage()).isEqualTo("user " + user.getUserName() + " already exists, pick other userName");
    }

    @Test
    void when_findUserById_then_returnUser() {
        //given
        when(userRepo.findById(1L)).thenReturn(Optional.ofNullable(user));
        //when
        UserEntity foundUser = userService.findById(1L);
        //then
        assertThat(foundUser.getUserName()).isEqualTo(user.getUserName());
        assertThat(foundUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(foundUser.getId()).isEqualTo(user.getId());
    }

    @Test
    void when_findUserById_then_UserNotFoundException() {
        //given
        Long wrongId = 2L;
        when(userRepo.findById(wrongId)).thenReturn(Optional.empty());
        //when
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.findById(wrongId));
        //then
        assertThat(exception.getMessage()).isEqualTo("User with id: " + wrongId + " not found");
    }

    @Test
    void when_findUserByUserName_then_return_user() {
        //given
        when(userRepo.findByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));
        //then
        UserEntity foundUser = userService.findUserByUsername(user.getUsername());
        //then
        assertThat(foundUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(foundUser.getUserName()).isEqualTo(user.getUserName());
    }

    @Test
    void when_findUserByUserName_then_UserNotFoundException() {
        //given
        String wrongUserName = "WrongUserName";
        when(userRepo.findByUsername(wrongUserName)).thenReturn(Optional.empty());
        //when
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.findUserByUsername(wrongUserName));
        //then
        assertThat(exception.getMessage()).isEqualTo("User : " + wrongUserName + " doesn't exists");
    }

    @Test
    void when_saveUser_then_returnUser() {
        //given
        when(userRepo.save(user)).thenReturn(user);
        //when
        UserEntity savedUser = userService.updateUser(user);
        //then
        assertThat(savedUser.getUserName()).isEqualTo(user.getUserName());
        assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    void when_saveNullUser_then_WrongInputException() {
        //given
        user = null;
        //when
        WrongInputException exception = assertThrows(WrongInputException.class, () -> userService.updateUser(user));
        //then
        assertThat(exception.getMessage()).isEqualTo("User cannot be null");
    }
    @Test
    void when_saveEmptyPasswordUser_then_WrongInputException() {
        //given
        user.setPassword("");
        //when
        WrongInputException exception = assertThrows(WrongInputException.class, () -> userService.updateUser(user));
        //then
        assertThat(exception.getMessage()).isEqualTo("userName or Password cannot be empty");
    }
    @Test
    void when_saveBlankPasswordUser_then_WrongInputException() {
        //given
        user.setPassword("  ");
        //when
        WrongInputException exception = assertThrows(WrongInputException.class, () -> userService.updateUser(user));
        //then
        assertThat(exception.getMessage()).isEqualTo("userName or Password cannot be empty");
    }
    @Test
    void when_saveNullPasswordUser_then_WrongInputException() {
        //given
        user.setPassword(null);
        //when
        WrongInputException exception = assertThrows(WrongInputException.class, () -> userService.updateUser(user));
        //then
        assertThat(exception.getMessage()).isEqualTo("userName or Password cannot be empty");
    }
    @Test
    void when_saveEmptyUsernameUser_then_WrongInputException() {
        //given
        user.setUserName("");
        //when
        WrongInputException exception = assertThrows(WrongInputException.class, () -> userService.updateUser(user));
        //then
        assertThat(exception.getMessage()).isEqualTo("userName or Password cannot be empty");
    }
    @Test
    void when_saveBlankUsernameUser_then_WrongInputException() {
        //given
        user.setUserName("   ");
        //when
        WrongInputException exception = assertThrows(WrongInputException.class, () -> userService.updateUser(user));
        //then
        assertThat(exception.getMessage()).isEqualTo("userName or Password cannot be empty");
    }
    @Test
    void when_saveNullUsernameUser_then_WrongInputException() {
        //given
        user.setUserName(null);
        //when
        WrongInputException exception = assertThrows(WrongInputException.class, () -> userService.updateUser(user));
        //then
        assertThat(exception.getMessage()).isEqualTo("userName or Password cannot be empty");
    }

    @Test
    void when_deleteUserByName_then_UserRepoDelete(){
        //given
        UserService serviceSpy = spy(userService);
        doReturn(user).when(serviceSpy).findUserByUsername("username");
        //when
        serviceSpy.deleteUserByUserName("username");
        //then
        verify(userRepo).delete(user);
    }

    @Test
    void when_deleteUserByName_then_throwUserNotFoundException(){
        //given
        String username = "wrongName";
        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());
        //when
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,() -> userService.deleteUserByUserName(username));
        //then
        assertThat(exception.getMessage()).isEqualTo("User : " + username + " doesn't exists");
    }


}
