package com.jakub.weather.repo;

import com.jakub.weather.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {

    @Query(value = "Select * from role where role=?1", nativeQuery = true)
    Optional<Role> findByName(String roleName);
}
