package com.jakub.weather.repo;

import com.jakub.weather.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {

    @Query(value = "SELECT r FROM Role r where r.role = :roleName")
    Optional<Role> findByName(String roleName);
}
