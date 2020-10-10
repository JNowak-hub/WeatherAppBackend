package com.jakub.weather.service;

import com.jakub.weather.exceptions.RoleNotFoundException;
import com.jakub.weather.model.user.Role;
import com.jakub.weather.repo.RoleRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    RoleRepo roleRepo;

    public RoleService(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public Role getRoleByName(String roleName){
        Optional<Role> role = roleRepo.findByName(roleName);
        if(role.isEmpty()){
            throw new RoleNotFoundException("No such role as " + roleName);
        }
        return role.get();
    }
}
