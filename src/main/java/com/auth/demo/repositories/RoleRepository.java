package com.auth.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth.demo.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
    
    Role findByName(String name); 

}