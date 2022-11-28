package com.auth.demo.controller;


import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.demo.dto.RoleDTO;
import com.auth.demo.model.Role;
import com.auth.demo.service.RoleService;

@RestController
@RequestMapping(value = "/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveRole(@RequestBody RoleDTO roleDTO) {
        Role role = new Role(roleDTO.getName());
        Role savedRole = roleService.saveRole(role);
        JSONObject roleJson = new JSONObject(savedRole);
        return new ResponseEntity<String>(roleJson.toString(), HttpStatus.CREATED);
    
    }
}
