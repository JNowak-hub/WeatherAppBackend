package com.jakub.weather.service.unit;

import com.jakub.weather.exceptions.RoleNotFoundException;
import com.jakub.weather.model.user.Role;
import com.jakub.weather.repo.RoleRepo;
import com.jakub.weather.service.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RoleServiceTest {
    @Mock
    private RoleRepo repo;
    @InjectMocks
    private RoleService roleService;
    @Test
    void when_getRoleByName_then_returnRole(){
        //given
        Role role = new Role();
        role.setRole("User");
        when(repo.findByName("User")).thenReturn(Optional.ofNullable(role));
        //when
        Role roleByName = roleService.getRoleByName("User");
        //then
        assertThat(roleByName).isEqualTo(role);
    }

    @Test
    void when_getRoleByName_than_throwsRoleNotFoundException(){
        //given
        Role role = new Role();
        role.setRole("User");
        when(repo.findByName("User")).thenReturn(Optional.ofNullable(null));
        //when
        RoleNotFoundException roleNotFoundException = assertThrows(RoleNotFoundException.class, () -> roleService.getRoleByName("User"));
        //then
        assertThat(roleNotFoundException.getMessage()).isEqualTo("No such role as User");
    }

}
