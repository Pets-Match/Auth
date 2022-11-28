package com.auth.demo.service;

import org.springframework.stereotype.Service;

import com.auth.demo.model.Role;
import com.auth.demo.repositories.RoleRepository;

@Service
public class RoleService {
    
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public Role saveRole(Role role){
        return roleRepository.save(role);
    }


}
